package com.notayessir.queue.route.service;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.notayessir.MatchClient;
import com.notayessir.MatchClientConfig;
import com.notayessir.bo.MatchCommandBO;
import com.notayessir.common.constant.EnumFieldDeleted;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.common.web.BusinessException;
import com.notayessir.queue.common.constant.EnumResponseOfQueue;
import com.notayessir.queue.route.bo.CreateRouteInfoReqBO;
import com.notayessir.queue.route.bo.CreateRouteInfoRespBO;
import com.notayessir.queue.route.constant.EnumOperationStatus;
import com.notayessir.queue.route.entity.MatchConfig;
import com.notayessir.queue.route.entity.RequestRecord;
import com.notayessir.queue.route.entity.RouteInfo;
import com.notayessir.queue.route.vo.CreateRouteInfoReq;
import com.notayessir.queue.route.vo.CreateRouteInfoResp;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.api.order.mq.OrderEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class FacadeRouteService {



    @Autowired
    private IRouteInfoService iRouteInfoService;



    @Autowired
    private IRequestRecordService iRequestRecordService;

    @Autowired
    private TransactionTemplate txTemplate;


    @Autowired
    private IMatchConfigService iMatchConfigService;


    private final Map<String, MatchClient> clientMap = new HashMap<>();

    @PostConstruct
    public void init(){
        List<MatchConfig> matchConfigs = iMatchConfigService.findAllMatchConfigs();
        if (CollectionUtil.isEmpty(matchConfigs)){
            log.info("matchConfigs is empty.");
            return;
        }
        for (MatchConfig matchConfig : matchConfigs) {
            if (clientMap.containsKey(matchConfig.getName())){
                continue;
            }
            List<String> addresses = Arrays.asList(matchConfig.getGroupAddress().split("[,;|]"));
            MatchClientConfig config = MatchClientConfig.builder()
                    .addresses(addresses).groupId(matchConfig.getGroupId())
                    .build();
            MatchClient matchClient = new MatchClient(config);
            matchClient.connect();
            clientMap.put(matchConfig.getName(), matchClient);
        }

    }

    @PreDestroy
    public void destroy(){
        try {
            Collection<MatchClient> clients = clientMap.values();
            for (MatchClient client : clients) {
                client.close();
            }
        }catch (IOException e){
            log.warn("fail to close client.");
        }
    }


    private RequestRecord buildRequestRecord(OrderEvent event) {
        RequestRecord record = new RequestRecord();
        record.setId(IdUtil.getSnowflakeNextId());
        record.setRequestId(event.getRequestId());
        record.setCommandType(event.getCommand());
        record.setOrderId(event.getOrderId());
        record.setCreateTime(LocalDateTime.now());
        record.setOpStatus(EnumOperationStatus.UNDONE.getCode());
        record.setEventContent(JSONObject.toJSONString(event));
        record.setVersion(EnumFieldVersion.INIT.getCode());
        return record;
    }


    public void handleOrderEvent(OrderEvent event) {
        if (iRequestRecordService.countRequestRecord(event.getRequestId()) > 0){
            // TODO send alarm
            return;
        }
        RequestRecord requestRecord = buildRequestRecord(event);
        txTemplate.executeWithoutResult( transactionStatus -> {
            iRequestRecordService.createRequest(requestRecord);
            doHandleOrderEvent(event);
        });

    }

    private Long doHandleOrderEvent(OrderEvent event) {
        RouteInfo routeInfo = iRouteInfoService.findByCoinId(event.getCoinId());
        if (Objects.isNull(routeInfo)){
            log.warn("no route info about coin id:{}", event.getCoinId());
            throw new BusinessException(EnumResponseOfQueue.ROUTE_NOT_EXIST.getCode(), EnumResponseOfQueue.ROUTE_NOT_EXIST.getMessage());
        }
        // route info seem as a load balance
        // send request to match engine
        try {

            MatchClient matchClient = clientMap.get(routeInfo.getRouteTo());
            if (Objects.isNull(matchClient)){
                throw new BusinessException(EnumResponseOfQueue.FAIL_TO_ROUTE.getCode(), EnumResponseOfQueue.FAIL_TO_ROUTE.getMessage());
            }
            MatchCommandBO commandBO = buildMatchCommandBO(event);
            Long result = matchClient.sendSync(commandBO);
            return result ;
        }catch (Exception e){
            // catching A ex means match engine has something error internal
            log.warn("error occur when send command to match engine:{}", event.getCoinId(), e);
            throw new BusinessException(EnumResponseOfQueue.FAIL_TO_ROUTE.getCode(), EnumResponseOfQueue.FAIL_TO_ROUTE.getMessage());

        }
    }

    private MatchCommandBO buildMatchCommandBO(OrderEvent event) {
        MatchCommandBO commandBO = new MatchCommandBO();
        commandBO.setEntrustSide(EnumEntrustSide.getEntrustSide(event.getEntrustSide()).getCode());
        commandBO.setEntrustPrice(event.getEntrustPrice());
        commandBO.setEntrustQty(event.getEntrustQty());
        commandBO.setEntrustAmount(event.getEntrustAmount());
        commandBO.setEntrustType(event.getEntrustType());
        commandBO.setEntrustProp(event.getEntrustProp());
        commandBO.setCoinId(event.getCoinId());
        commandBO.setOrderId(event.getOrderId());
        commandBO.setQuoteScale(event.getQuoteScale());
        commandBO.setCommand(event.getCommand());
        commandBO.setRequestId(event.getRequestId());
        return commandBO;
    }


    public CreateRouteInfoRespBO createRouteInfo(CreateRouteInfoReqBO req) {
        RouteInfo routeInfo = buildNewRouteInfo(req);
        iRouteInfoService.createRouteInfo(routeInfo);
        CreateRouteInfoRespBO resp = new CreateRouteInfoRespBO();
        resp.setId(routeInfo.getId());
        return resp;
    }

    private RouteInfo buildNewRouteInfo(CreateRouteInfoReqBO req) {
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setId(IdUtil.getSnowflakeNextId());
        routeInfo.setRouteTo(req.getRouteTo());
        routeInfo.setCoinId(req.getCoinId());
        LocalDateTime now = LocalDateTime.now();
        routeInfo.setCreateTime(now);
        routeInfo.setUpdateTime(now);
        routeInfo.setDeleted(EnumFieldDeleted.NOT_DELETED.getCode());
        routeInfo.setVersion(EnumFieldVersion.INIT.getCode());
        return routeInfo;
    }

    private CreateRouteInfoReqBO toCreateRouteInfoReqBO(CreateRouteInfoReq req) {
        CreateRouteInfoReqBO reqBO = new CreateRouteInfoReqBO();
        reqBO.setCoinId(req.getCoinId());
        reqBO.setRouteTo(req.getRouteTo());
        reqBO.setIp(req.getIp());

        reqBO.setUserId(req.getUserId());
        reqBO.setRequestId(req.getRequestId());
        reqBO.setRequestSource(EnumRequestSource.ADMIN);
        return reqBO;
    }

    public CreateRouteInfoResp adminCreateRouteInfo(CreateRouteInfoReq req) {
        CreateRouteInfoReqBO reqBO = toCreateRouteInfoReqBO(req);

        CreateRouteInfoRespBO respBO = createRouteInfo(reqBO);

        return toCreateRouteInfoResp(respBO);
    }

    private CreateRouteInfoResp toCreateRouteInfoResp(CreateRouteInfoRespBO respBO) {
        CreateRouteInfoResp resp = new CreateRouteInfoResp();
        resp.setId(respBO.getId());
        return resp;
    }




}
