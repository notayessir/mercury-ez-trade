package com.notayessir.user.order.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.bo.OrderItemBO;
import com.notayessir.common.constant.EnumFieldDeleted;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.common.web.BusinessException;
import com.notayessir.constant.EnumMatchCommand;
import com.notayessir.user.api.order.constant.EnumEntrustProp;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.api.order.constant.EnumEntrustType;
import com.notayessir.user.api.order.mq.OrderTopic;
import com.notayessir.user.api.order.mq.OrderEvent;
import com.notayessir.user.coin.bo.CheckAccountResultBO;
import com.notayessir.user.coin.bo.CheckCoinResultBO;
import com.notayessir.user.coin.entity.CoinPair;
import com.notayessir.user.coin.service.ICoinPairService;
import com.notayessir.user.common.mq.KafkaMQService;
import com.notayessir.user.common.vo.EnumUserResponse;
import com.notayessir.user.order.bo.*;
import com.notayessir.user.order.constant.EnumOperationStatus;
import com.notayessir.user.order.constant.EnumOrderStatus;
import com.notayessir.user.order.entity.*;
import com.notayessir.user.order.handler.OrderExecutedHandler;
import com.notayessir.user.order.vo.*;
import com.notayessir.user.user.entity.Account;
import com.notayessir.user.user.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FacadeOrderService {

    @Autowired
    private ICoinPairService iCoinPairService;

    @Autowired
    private IAccountService iAccountService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IClinchRecordService iClinchRecordService;


    @Autowired
    private TransactionTemplate txTemplate;

    @Autowired
    private KafkaMQService kafkaMQService;

    @Autowired
    private IRequestRecordService iRequestRecordService;


    @Autowired
    private IMqRecordService iMqRecordService;

    @Autowired
    private IClinchSequenceService iClinchSequenceService;




    private CreateOrderRespBO createOrder(CreateOrderReqBO req) {
        // checkAndInit idempotent
        RequestRecord existRecord = iRequestRecordService.findByRequestId(req.getRequestId());
        if (Objects.nonNull(existRecord)){
            CreateOrderRespBO resp = new CreateOrderRespBO();
            resp.setOrderId(existRecord.getBusinessId());
            return resp;
        }
        // checkAndInit coin
        CheckCoinResultBO checkProductResult = iCoinPairService.checkCoin(req);

        // checkAndInit account
        CoinPair coinPair = checkProductResult.getCoinPair();

        CheckAccountResultBO checkAccountResultBO = iAccountService.checkAccount(req, coinPair);
        Account account = checkAccountResultBO.getAccount();


        // prepare order
        Order order = buildNewOrder(req, account, coinPair);
        RequestRecord requestRecord = buildNewRequestRecord(req, order);
        MqRecord mqRecord = buildNewMqRecord(order);

        BigDecimal toHold = StringUtils.equalsIgnoreCase(req.getSide(), EnumEntrustSide.SELL.getSide())
                ? req.getEntrustQty() : req.getEntrustAmount();
        txTemplate.executeWithoutResult(transactionStatus -> {
            iRequestRecordService.createRequest(requestRecord);
            iAccountService.moveAvailableToHold(account, toHold);
            iOrderService.createOrder(order);
            iMqRecordService.createMqRecord(mqRecord);
        });

        // send to queue
        OrderEvent orderEvent = buildOrderEvent(order, coinPair, mqRecord.getId());
        kafkaMQService.sendMessage(OrderTopic.CREATE_ORDER_EVENT_TOPIC, orderEvent);

        txTemplate.executeWithoutResult(transactionStatus -> {
            iOrderService.submittedOrder(order);
            iMqRecordService.finishMqRecord(mqRecord);
        });
        CreateOrderRespBO resp = new CreateOrderRespBO();
        resp.setOrderId(order.getId());
        return resp;
    }

    private MqRecord buildNewMqRecord(Order order) {
        MqRecord record = new MqRecord();
        record.setId(IdUtil.getSnowflakeNextId());
        record.setOrderId(order.getId());
        record.setOpType(EnumOrderStatus.CREATE.getCode());
        record.setOpStatus(EnumOperationStatus.UNDONE.getCode());
        record.setVersion(EnumFieldVersion.INIT.getCode());
        LocalDateTime now = LocalDateTime.now();
        record.setCreateTime(now);
        record.setUpdateTime(now);

        return record;
    }

    private RequestRecord buildNewRequestRecord(CreateOrderReqBO req, Order order) {
        RequestRecord record = new RequestRecord();
        record.setRequestId(req.getRequestId());
        LocalDateTime now = LocalDateTime.now();
        record.setCreateTime(now);
        record.setId(IdUtil.getSnowflakeNextId());
        record.setBusinessId(order.getId());
        return record;
    }


    private Order buildNewOrder(CreateOrderReqBO req, Account account, CoinPair coinPair) {
        Order order = new Order();
        if (req.getEntrustType() == EnumEntrustType.MARKET.getType()){
            order.setEntrustPrice("0");

        } else if (req.getEntrustType() == EnumEntrustType.NORMAL_LIMIT.getType()
                || req.getEntrustType() == EnumEntrustType.PREMIUM_LIMIT.getType()){
            order.setEntrustPrice(req.getEntrustPrice().toString());

        }
        order.setEntrustAmount(req.getEntrustAmount().toString());
        order.setEntrustQty(req.getEntrustQty().toString());

        EnumEntrustProp prop = EnumEntrustProp.getByType(req.getEntrustProp());
        if (Objects.nonNull(prop)){
            order.setEntrustProp(prop.name());
        }

        order.setId(IdUtil.getSnowflake().nextId());
        order.setTradeDate(LocalDate.now());
        order.setUserId(req.getUserId());
        order.setAccountId(account.getId());
        order.setEntrustSide(req.getSide());
        order.setCoinId(coinPair.getId());
        order.setCoinBaseCurrency(coinPair.getBaseCurrency());
        order.setCoinQuoteCurrency(coinPair.getQuoteCurrency());
        order.setClinchAmount("0");
        order.setClinchQty("0");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setDeleted(EnumFieldDeleted.NOT_DELETED.getCode());
        order.setVersion(EnumFieldVersion.INIT.getCode());
        order.setOrderStatus(EnumOrderStatus.CREATE.getCode());
        order.setEntrustType(req.getEntrustType());
        return order;
    }

    private OrderEvent buildOrderEvent(Order order, CoinPair coinPair, Long id) {

        return OrderEvent.builder()
                .userId(order.getUserId())
                .orderId(order.getId())
                .entrustType(order.getEntrustType())
                .entrustPrice(new BigDecimal(order.getEntrustPrice()))
                .entrustSide(order.getEntrustSide())
                .entrustQty(new BigDecimal(order.getEntrustQty()))
                .command(EnumMatchCommand.PLACE.getCode())
                .coinId(order.getCoinId())
                .entrustAmount(new BigDecimal(order.getEntrustAmount()))
                .timestamp(System.currentTimeMillis())
                .quoteScale(coinPair.getQuoteScale())
                .baseScale(coinPair.getBaseScale())
                .requestId(id)
                .build();
    }





    private CancelOrderRespBO cancelOrder(CancelOrderReqBO req) {
        Order order = iOrderService.findOrder(req.getOrderId(), req.getUserId());
        if (Objects.isNull(order)){
            throw new BusinessException(EnumUserResponse.RECORD_NOT_EXIST.getCode(), EnumUserResponse.RECORD_NOT_EXIST.getMessage());
        }
        Integer orderStatus = order.getOrderStatus();
        EnumOrderStatus enumOrderStatus = EnumOrderStatus.getEnumOrderStatus(order.getOrderStatus());
        if (Objects.isNull(enumOrderStatus) || !enumOrderStatus.isCancelable()){
            throw new BusinessException(EnumUserResponse.ORDER_FAIL_TO_CANCEL.getCode(), EnumUserResponse.ORDER_FAIL_TO_CANCEL.getMessage());
        }


        if (EnumOrderStatus.CREATE.getCode() == orderStatus){
            String currency = order.getEntrustSide().equals(EnumEntrustSide.SELL.getSide()) ?
                    order.getCoinQuoteCurrency() : order.getCoinBaseCurrency();
            Account account = iAccountService.getAccount(order.getUserId(), currency);
            txTemplate.executeWithoutResult(transactionStatus -> {
                iAccountService.moveHoldToAvailable(account, new BigDecimal(order.getEntrustAmount()));
                iOrderService.cancelOrder(order);
            });
            return CancelOrderRespBO.builder().orderId(req.getOrderId()).build();
        }

        if (EnumOrderStatus.isProcessingStatus(orderStatus)) {
            // notify match engine to cancel the order
            MqRecord mqRecord = buildCancellingMqRecord(order);
            iMqRecordService.createMqRecord(mqRecord);
            OrderEvent event = OrderEvent.builder()
                    .orderId(order.getId())
                    .command(EnumMatchCommand.CANCEL.getCode())
                    .timestamp(System.currentTimeMillis())
                    .requestId(mqRecord.getId())
                    .coinId(order.getCoinId())
                    .build();
            boolean success = kafkaMQService.sendMessage(OrderTopic.CREATE_ORDER_EVENT_TOPIC, event);
            txTemplate.executeWithoutResult(transactionStatus -> {
                iOrderService.cancellingOrder(order);
                iMqRecordService.finishMqRecord(mqRecord);
            });

        }
        throw new BusinessException(EnumUserResponse.ORDER_CANCELLING.getCode(), EnumUserResponse.ORDER_CANCELLING.getMessage());
    }

    private MqRecord buildCancellingMqRecord(Order order) {
        MqRecord record = new MqRecord();
        record.setId(IdUtil.getSnowflakeNextId());
        record.setOrderId(order.getId());
        record.setOpType(EnumOrderStatus.CANCELLING.getCode());
        record.setOpStatus(EnumOperationStatus.UNDONE.getCode());
        record.setVersion(EnumFieldVersion.INIT.getCode());
        LocalDateTime now = LocalDateTime.now();
        record.setCreateTime(now);
        record.setUpdateTime(now);

        return record;
    }



    public void handleOrderExecutedEvent(MatchResultBO event) {
        // unique key: global_sequence
        if (iClinchSequenceService.countClinchSequence(event.getGlobalSequence()) > 0){
            return;
        }

        ClinchSequence clinchSequence = buildClinchSequence(event);
        txTemplate.executeWithoutResult(transactionStatus -> {

            iClinchSequenceService.createClinchSequence(clinchSequence);
            OrderItemBO takerOrder = event.getTakerOrder();



            OrderExecutedHandler handler = OrderExecutedHandler.getCommandExecutedHandler(takerOrder.getMatchStatus());
            handler.handle(event);

        });
    }


    private ClinchSequence buildClinchSequence(MatchResultBO event) {
        ClinchSequence record = new ClinchSequence();

        OrderItemBO takerOrder = event.getTakerOrder();
        record.setId(IdUtil.getSnowflakeNextId());
        record.setOrderId(takerOrder.getOrderId());
        record.setGlobalSequence(event.getGlobalSequence());
        record.setTxSequence(event.getTxSequence());

        record.setOpStatus(EnumOperationStatus.UNDONE.getCode());
        LocalDateTime now = LocalDateTime.now();
        record.setUpdateTime(now);
        record.setCreateTime(now);
        record.setVersion(EnumFieldVersion.INIT.getCode());
        record.setEventContent(JSONObject.toJSONString(event));


        return record;
    }


    public CreateOrderResp apiCreateOrder(CreateOrderReq req) {
        CreateOrderReqBO reqBO = toCreateOrderReqBO(req);

        CreateOrderRespBO respBO = createOrder(reqBO);

        return toCreateOrderResp(respBO);
    }

    private CreateOrderResp toCreateOrderResp(CreateOrderRespBO respBO) {
        CreateOrderResp resp = new CreateOrderResp();
        resp.setOrderId(respBO.getOrderId());
        return resp;
    }

    private CreateOrderReqBO toCreateOrderReqBO(CreateOrderReq req) {
        CreateOrderReqBO reqBO = new CreateOrderReqBO();
        reqBO.setEntrustQty(req.getEntrustQty());
        reqBO.setEntrustPrice(req.getEntrustPrice());
        reqBO.setEntrustType(req.getEntrustType());
        reqBO.setSide(req.getSide());
        reqBO.setBasePrice(req.getBasePrice());
        reqBO.setCoinId(req.getCoinId());
        reqBO.setEntrustAmount(req.getEntrustAmount());
        reqBO.setEntrustProp(req.getEntrustProp());
        reqBO.setIp(req.getIp());
        reqBO.setRequestSource(EnumRequestSource.API);
        reqBO.setUserId(req.getUserId());
        reqBO.setRequestId(req.getRequestId());

        return reqBO;
    }

    public PatchOrderResp apiCancelOrder(PatchOrderReq req) {
        // default patch code is cancel
        CancelOrderReqBO reqBO = toCancelOrderReqBO(req);

        CancelOrderRespBO respBO = cancelOrder(reqBO);

        return toCancelOrderResp(respBO);

    }

    private PatchOrderResp toCancelOrderResp(CancelOrderRespBO respBO) {
        PatchOrderResp resp = new PatchOrderResp();
        resp.setOrderId(respBO.getOrderId());
        return resp;
    }

    private CancelOrderReqBO toCancelOrderReqBO(PatchOrderReq req) {
        CancelOrderReqBO reqBO = new CancelOrderReqBO();
        reqBO.setIp(req.getIp());
        reqBO.setRequestId(req.getRequestId());
        reqBO.setUserId(req.getUserId());
        reqBO.setOrderId(req.getOrderId());
        reqBO.setRequestSource(EnumRequestSource.API);

        return reqBO;
    }

    public FindOrderResp apiFindOrder(FindOrderReq req) {
        FindOrderReqBO reqBO = toFindOrderReqBO(req);


        FindOrderRespBO respBO = findOrder(reqBO);

        return toFindOrderResp(respBO);
    }

    private FindOrderResp toFindOrderResp(FindOrderRespBO respBO) {

        return new FindOrderResp(respBO.getOrder(), respBO.getClinchRecords());
    }

    private FindOrderRespBO findOrder(FindOrderReqBO reqBO) {
        Order order = iOrderService.findOrder(reqBO.getOrderId(), reqBO.getUserId());
        if (Objects.isNull(order)){
            throw new BusinessException(EnumUserResponse.RECORD_NOT_EXIST.getCode(), EnumUserResponse.RECORD_NOT_EXIST.getMessage());
        }
        List<ClinchRecord> clinchRecords = iClinchRecordService.findByOrderId(reqBO.getOrderId());

        return new FindOrderRespBO(order, clinchRecords);
    }

    private FindOrderReqBO toFindOrderReqBO(FindOrderReq req) {
        FindOrderReqBO reqBO = new FindOrderReqBO();
        reqBO.setUserId(req.getUserId());
        reqBO.setOrderId(req.getOrderId());
        return reqBO;
    }

    public BasePageResp<FindOrderResp> apiFindOrders(BasePageReq<FindOrdersReq> req) {
        BasePageReq<FindOrdersReqBO> reqBO = toBasePageFindOrdersReq(req);

        BasePageResp<FindOrderRespBO> respBO = findOrders(reqBO);

        return toBasePageRespFindOrderResp(respBO);
    }

    private BasePageResp<FindOrderResp> toBasePageRespFindOrderResp(BasePageResp<FindOrderRespBO> respBO) {
        BasePageResp<FindOrderResp> target = new BasePageResp<>();
        BeanUtils.copyProperties(respBO, target);
        List<FindOrderRespBO> records = respBO.getRecords();
        if (CollectionUtil.isNotEmpty(records)){
            List<FindOrderResp> list = new ArrayList<>(records.size());
            for (FindOrderRespBO record : records) {
                list.add(new FindOrderResp(record.getOrder(), record.getClinchRecords()));
            }
            target.setRecords(list);
        }
        return target;
    }

    private BasePageResp<FindOrderRespBO> findOrders(BasePageReq<FindOrdersReqBO> reqBO) {
        Page<Order> page = iOrderService.findOrders(reqBO);
        BasePageResp<FindOrderRespBO> resp = new BasePageResp<>(page.getTotal(), page.getSize(), page.getCurrent());
        List<Order> records = page.getRecords();
        if (CollectionUtil.isNotEmpty(records)){
            List<FindOrderRespBO> list = new ArrayList<>(records.size());
            for (Order record : records) {
                list.add(new FindOrderRespBO(record));
            }
            resp.setRecords(list);
        }
        return resp;
    }


    private BasePageReq<FindOrdersReqBO> toBasePageFindOrdersReq(BasePageReq<FindOrdersReq> req) {
        FindOrdersReq query = req.getQuery();
        FindOrdersReqBO queryTarget = new FindOrdersReqBO();
        BeanUtils.copyProperties(query, queryTarget);


        BasePageReq<FindOrdersReqBO> target = new BasePageReq<>();
        req.setRequestSource(EnumRequestSource.API);
        BeanUtils.copyProperties(req, target);
        target.setQuery(queryTarget);
        return target;
    }

    public void orderCancelCompensate() {
        List<MqRecord> records = iMqRecordService.findToFinishRecord(EnumOrderStatus.CANCELLING.getCode(), 77);
        if (CollectionUtils.isEmpty(records)){
            return;
        }
        for (MqRecord record : records) {
            doOrderCancelCompensate(record);
        }
    }

    private void doOrderCancelCompensate(MqRecord record){
        Order order = iOrderService.getById(record.getOrderId());
        if (EnumOrderStatus.isEndedStatus(order.getOrderStatus())){
            iMqRecordService.finishMqRecord(record);
            return;
        }
        // send to queue
        OrderEvent event = OrderEvent.builder()
                .command(EnumMatchCommand.CANCEL.getCode())
                .orderId(record.getId())
                .build();
        kafkaMQService.sendMessage(OrderTopic.CREATE_ORDER_EVENT_TOPIC, event);
        txTemplate.executeWithoutResult(transactionStatus -> {
            iOrderService.cancellingOrder(order);
            iMqRecordService.finishMqRecord(record);
        });
    }

    public void orderCreateCompensate() {
        List<MqRecord> records = iMqRecordService.findToFinishRecord(EnumOrderStatus.CREATE.getCode(), 77);
        if (CollectionUtils.isEmpty(records)){
            return;
        }
        for (MqRecord record : records) {

            doOrderCreateCompensate(record);

        }

    }

    private void doOrderCreateCompensate(MqRecord record){
        Order order = iOrderService.getById(record.getOrderId());
        if (EnumOrderStatus.isEndedStatus(order.getOrderStatus())){
            iMqRecordService.finishMqRecord(record);
            return;
        }
        // send to queue
        CoinPair coinPair = iCoinPairService.getById(order.getCoinId());
        OrderEvent event = buildOrderEvent(order, coinPair, record.getId());
        kafkaMQService.sendMessage(OrderTopic.CREATE_ORDER_EVENT_TOPIC, event);
        txTemplate.executeWithoutResult(transactionStatus -> {
            iOrderService.submittedOrder(order);
            iMqRecordService.finishMqRecord(record);
        });
    }


}
