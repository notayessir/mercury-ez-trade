package com.notayessir.quote.spot.service;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.bo.OrderItemBO;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.vo.resp.BasePageResp;
import com.notayessir.quote.api.spot.mq.*;
import com.notayessir.quote.common.mq.KafkaMQService;
import com.notayessir.quote.spot.bo.GetKlineReqBO;
import com.notayessir.quote.spot.bo.GetKlineRespBO;
import com.notayessir.quote.spot.entity.KLine;
import com.notayessir.quote.spot.entity.TickRecord;
import com.notayessir.quote.spot.handler.HandicapHandler;
import com.notayessir.quote.spot.vo.GetKlineReq;
import com.notayessir.quote.spot.vo.GetKlineResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacadeSpotQuoteService {

    @Autowired
    private ITickRecordService iTickRecordService;

    @Autowired
    private IKLineService ikLineService;

    @Autowired
    private IHandicapService iHandicapService;

    @Autowired
    private TransactionTemplate txTemplate;

    @Autowired
    private KafkaMQService kafkaMQService;


    public void handleQuoteUpdateEvent(MatchResultBO event) {
        if (iTickRecordService.countTickRecord(event.getGlobalSequence()) > 0){
            return;
        }


        txTemplate.executeWithoutResult(transactionStatus -> {
            // 1. save tick record
            QTickRecordDTO qTickRecord = createTickRecord(event);

            // 2. update order book
            OrderItemBO takerOrder = event.getTakerOrder();
            HandicapHandler handler = HandicapHandler.getInstance(takerOrder.getMatchStatus());
            List<QOrderBookDTO> qOrderBooks = handler.handleHandicapUpdatedEventWithReturn(event);

            // 3. update k line
            List<QKLineDTO> qKLines = handleKLineUpdatedEvent(event);

            // 4. publish
            QuoteUpdatedEvent quoteUpdatedEvent = buildQuoteUpdatedEvent(event.getGlobalSequence(), qTickRecord, qOrderBooks, qKLines);
            kafkaMQService.sendMessage(QuoteTopic.QUOTE_UPDATED_TOPIC, quoteUpdatedEvent);
        });
    }

    private QuoteUpdatedEvent buildQuoteUpdatedEvent(Long globalSequence, QTickRecordDTO qTickRecord, List<QOrderBookDTO> qOrderBooks, List<QKLineDTO> qKLines) {
        QuoteUpdatedEvent event = new QuoteUpdatedEvent();
        event.setRequestId(globalSequence);
        event.setQKLines(qKLines);
        event.setQTickRecord(qTickRecord);
        event.setQOrderBooks(qOrderBooks);
        return event;
    }

    private QTickRecordDTO createTickRecord(MatchResultBO event) {
        TickRecord tickRecord = buildTickRecord(event);
        iTickRecordService.createTickRecord(tickRecord);


        QTickRecordDTO target = new QTickRecordDTO();
        target.setCreateTime(tickRecord.getCreateTime());
        target.setGlobalSequence(tickRecord.getGlobalSequence());
        target.setTxSequence(tickRecord.getTxSequence());
        target.setTickTimestamp(tickRecord.getTickTimestamp());
        target.setClinchQty(tickRecord.getClinchQty());
        target.setClinchAmount(tickRecord.getClinchAmount());
        target.setCoinId(tickRecord.getCoinId());
        return target;
    }




    private List<QKLineDTO> handleKLineUpdatedEvent(MatchResultBO event) {
        BigDecimal qty = event.gatherClinchQty();
        if (qty.compareTo(BigDecimal.ZERO) <= 0){
            return new ArrayList<>(0);
        }
        List<KLine> kLines = ikLineService.handleKLineUpdatedEvent(event);
        List<QKLineDTO> target = new ArrayList<>(kLines.size());
        for (KLine kLine : kLines) {


            QKLineDTO item = new QKLineDTO();
            item.setUpdateTime(kLine.getUpdateTime());
            item.setCreateTime(kLine.getCreateTime());
            item.setVersion(kLine.getVersion());
            item.setClinchQty(kLine.getClinchQty());
            item.setClosePrice(kLine.getClosePrice());
            item.setClinchAmount(kLine.getClinchAmount());
            item.setLowPrice(kLine.getLowPrice());
            item.setHighPrice(kLine.getHighPrice());
            item.setOpenPrice(kLine.getOpenPrice());
            item.setStatisticStartTime(kLine.getStatisticStartTime());
            item.setTimeInterval(kLine.getTimeInterval());
            item.setCoinId(kLine.getCoinId());


            target.add(item);
        }
        return target;
    }





    private TickRecord buildTickRecord(MatchResultBO event) {
        TickRecord record = new TickRecord();

        record.setId(IdUtil.getSnowflakeNextId());
        record.setGlobalSequence(event.getGlobalSequence());
        record.setTxSequence(event.getTxSequence());

        BigDecimal amount = event.gatherClinchAmount();
        BigDecimal qty = event.gatherClinchQty();

        record.setCoinId(event.getCoinId());
        record.setClinchAmount(amount.toString());
        record.setClinchQty(qty.toString());
        record.setTickTimestamp(event.getTimestamp());
        record.setVersion(EnumFieldVersion.INIT.getCode());
        LocalDateTime now = LocalDateTime.now();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        record.setEvent(JSONObject.toJSONString(event));
        return record;
    }


    private BasePageResp<GetKlineRespBO> findKline(BasePageReq<GetKlineReqBO> pageReq) {
        Page<KLine> page = ikLineService.findKline(pageReq);
        return toBasePageGetKlineRespBO(page);
    }

    private BasePageResp<GetKlineRespBO> toBasePageGetKlineRespBO(Page<KLine> page) {
        List<GetKlineRespBO> list = new ArrayList<>(page.getRecords().size());
        for (KLine kLine : page.getRecords()){
            GetKlineRespBO target = new GetKlineRespBO();
            target.setId(kLine.getId());
            target.setCoinId(kLine.getCoinId());
            target.setTimeInterval(kLine.getTimeInterval());
            target.setStatisticStartTime(kLine.getStatisticStartTime());

            target.setOpenPrice(kLine.getOpenPrice());
            target.setHighPrice(kLine.getHighPrice());
            target.setLowPrice(kLine.getLowPrice());
            target.setClosePrice(kLine.getClosePrice());

            target.setClinchQty(kLine.getClinchQty());
            target.setClinchAmount(kLine.getClinchAmount());

            list.add(target);
        }

        BasePageResp<GetKlineRespBO> resp = new BasePageResp<>();
        resp.setCurrent(page.getCurrent());
        resp.setSize(page.getSize());
        resp.setTotal(page.getTotal());
        resp.setRecords(list);
        return resp;
    }

    private BasePageResp<GetKlineResp> toBasePageFindKlineResp(BasePageResp<GetKlineRespBO> pageResp) {
        List<GetKlineResp> list = new ArrayList<>(pageResp.getRecords().size());
        for (GetKlineRespBO kLine : pageResp.getRecords()){
            GetKlineResp target = new GetKlineResp();
            target.setId(kLine.getId());
            target.setCoinId(kLine.getCoinId());
            target.setTimeInterval(kLine.getTimeInterval());
            target.setStatisticStartTime(kLine.getStatisticStartTime());

            target.setOpenPrice(kLine.getOpenPrice());
            target.setHighPrice(kLine.getHighPrice());
            target.setLowPrice(kLine.getLowPrice());
            target.setClosePrice(kLine.getClosePrice());

            target.setClinchQty(kLine.getClinchQty());
            target.setClinchAmount(kLine.getClinchAmount());

            list.add(target);
        }

        BasePageResp<GetKlineResp> resp = new BasePageResp<>();
        resp.setCurrent(pageResp.getCurrent());
        resp.setSize(pageResp.getSize());
        resp.setTotal(pageResp.getTotal());
        resp.setRecords(list);

        return resp;
    }

    private BasePageReq<GetKlineReqBO> toBasePageFindKlineReqBO(BasePageReq<GetKlineReq> pageReq) {
        BasePageReq<GetKlineReqBO> target = new BasePageReq<>();
        target.setPageNum(pageReq.getPageNum());
        target.setIp(pageReq.getIp());
        target.setUserId(pageReq.getUserId());
        target.setPageSize(pageReq.getPageSize());
        target.setRequestId(pageReq.getRequestId());

        GetKlineReq sourceQ = pageReq.getQuery();
        GetKlineReqBO targetQ = new GetKlineReqBO();
        targetQ.setRequestSource(EnumRequestSource.PUBLIC_API);
        targetQ.setInterval(sourceQ.getInterval());
        targetQ.setSymbol(sourceQ.getSymbol());

        target.setQuery(targetQ);
        return target;
    }

    public BasePageResp<GetKlineResp> publicApiFindKline(BasePageReq<GetKlineReq> pageReq) {
        BasePageReq<GetKlineReqBO> page = toBasePageFindKlineReqBO(pageReq);

        BasePageResp<GetKlineRespBO> pageResp = findKline(page);

        return toBasePageFindKlineResp(pageResp);
    }
}
