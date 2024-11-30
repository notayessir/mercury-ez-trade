package com.notayessir.quote.spot.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.constant.EnumRequestSource;
import com.notayessir.engine.api.bo.MatchResultBO;
import com.notayessir.engine.api.bo.OrderItemBO;
import com.notayessir.engine.api.constant.EnumEntrustSide;
import com.notayessir.quote.api.spot.mq.*;
import com.notayessir.quote.common.mq.KafkaMQService;
import com.notayessir.quote.spot.bo.GetKlineReqBO;
import com.notayessir.quote.spot.constant.EnumEventType;
import com.notayessir.quote.spot.entity.AggTradeRecord;
import com.notayessir.quote.spot.entity.Handicap;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacadeSpotQuoteService {

    @Autowired
    private ITickRecordService iTickRecordService;

    @Autowired
    private IAggTradeRecordService iAggTradeRecordService;

    @Autowired
    private IKLineService ikLineService;

    @Autowired
    private IHandicapService iHandicapService;

    @Autowired
    private TransactionTemplate txTemplate;

    @Autowired
    private KafkaMQService kafkaMQService;



    public DepthDTO generateDepth(Long coinId){
        List<Handicap> handicaps = iHandicapService.findHandicap(coinId);
        Map<Integer, List<Handicap>> groupedByEntrustSide = handicaps.stream()
                .collect(Collectors.groupingBy(Handicap::getEntrustSide));
        List<Handicap> buyHandicapList = groupedByEntrustSide.get(EnumEntrustSide.BUY.getCode());
        List<Handicap> sellHandicapList = groupedByEntrustSide.get(EnumEntrustSide.SELL.getCode());

        DepthDTO depthDTO = generateDepth(buyHandicapList, sellHandicapList);
        depthDTO.setCoinId(coinId);
        return depthDTO;
    }


    public DepthDTO generateDepth(List<Handicap> buyHandicapList, List<Handicap> sellHandicapList){
        Map<BigDecimal, BigDecimal> bid = generateDepth(buyHandicapList);
        Map<BigDecimal, BigDecimal> ask = generateDepth(sellHandicapList);

        DepthDTO depthDTO = new DepthDTO();
        depthDTO.setAsk(ask);
        depthDTO.setBid(bid);
        return depthDTO;
    }



    private Map<BigDecimal, BigDecimal> generateDepth(List<Handicap> handicaps) {
        Map<BigDecimal, BigDecimal> priceQtyMap = new TreeMap<>();
        for (Handicap handicap : handicaps) {
            BigDecimal qty = new BigDecimal(handicap.getQty());
            if (qty.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }
            BigDecimal price = new BigDecimal(handicap.getPrice());
            if (priceQtyMap.containsKey(price)){
                qty = priceQtyMap.get(price).add(qty);
            }
            priceQtyMap.put(price, qty);
        }
        return priceQtyMap;
    }


    private List<KLineDTO> buildKLineDTOList(List<KLine> kLines){
        if (CollectionUtil.isEmpty(kLines)){
            return Collections.emptyList();
        }

        List<KLineDTO> kLineDTOList = new ArrayList<>(kLines.size());
        for (KLine kLine : kLines) {
            KLineDTO kLineDTO = new KLineDTO();
            kLineDTO.setUpdateTime(kLine.getUpdateTime());
            kLineDTO.setCreateTime(kLine.getCreateTime());
            kLineDTO.setVersion(kLine.getVersion());
            kLineDTO.setClinchQty(kLine.getClinchQty());
            kLineDTO.setClosePrice(kLine.getClosePrice());
            kLineDTO.setClinchAmount(kLine.getClinchAmount());
            kLineDTO.setLowPrice(kLine.getLowPrice());
            kLineDTO.setHighPrice(kLine.getHighPrice());
            kLineDTO.setOpenPrice(kLine.getOpenPrice());
            kLineDTO.setStatisticStartTime(kLine.getStatisticStartTime());
            kLineDTO.setTimeInterval(kLine.getTimeInterval());
            kLineDTO.setCoinId(kLine.getCoinId());
            kLineDTOList.add(kLineDTO);
        }
        return kLineDTOList;
    }


    private AggTradeRecordDTO buildAggTradeRecordDTO(AggTradeRecord aggTradeRecord) {
        AggTradeRecordDTO target = new AggTradeRecordDTO();
        target.setCreateTime(aggTradeRecord.getCreateTime());
        target.setGlobalSequence(aggTradeRecord.getGlobalSequence());
        target.setTxSequence(aggTradeRecord.getTxSequence());
        target.setTickTimestamp(aggTradeRecord.getTickTimestamp());
        target.setClinchQty(aggTradeRecord.getClinchQty());
        target.setClinchAmount(aggTradeRecord.getClinchAmount());
        target.setCoinId(aggTradeRecord.getCoinId());
        return target;
    }



    private TickRecord buildTickRecord(MatchResultBO event, EnumEventType eventType) {
        TickRecord record = new TickRecord();

        record.setId(IdUtil.getSnowflakeNextId());
        record.setGlobalSequence(event.getGlobalSequence());
        record.setTxSequence(event.getTxSequence());

        record.setCoinId(event.getCoinId());
        record.setTickTimestamp(event.getTimestamp());
        record.setVersion(EnumFieldVersion.INIT.getCode());
        LocalDateTime now = LocalDateTime.now();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        record.setEventType(eventType.getType());
        return record;
    }


    private AggTradeRecord buildAggTradeRecord(MatchResultBO event) {
        AggTradeRecord record = new AggTradeRecord();

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


    private List<KLine> findKline(GetKlineReqBO reqBO) {
        return ikLineService.findKline(reqBO);
    }


    private List<GetKlineResp> toGetKlineRespBO(List<KLine> sourceList) {
        List<GetKlineResp> targetList = new ArrayList<>(sourceList.size());
        for (KLine kLine : sourceList){
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

            targetList.add(target);
        }

        return targetList;
    }

    private GetKlineReqBO toGetKlineReqBO(GetKlineReq sourceQ) {

        GetKlineReqBO targetQ = new GetKlineReqBO();
        targetQ.setRequestSource(EnumRequestSource.PUBLIC_API);
        targetQ.setInterval(sourceQ.getInterval());
        targetQ.setSymbol(sourceQ.getSymbol());
        targetQ.setEndTime(sourceQ.getEndTime());
        targetQ.setStartTime(sourceQ.getStartTime());
        targetQ.setCoinId(sourceQ.getCoinId());
        return targetQ;
    }

    public List<GetKlineResp> publicApiFindKline(GetKlineReq req) {
        GetKlineReqBO reqBO = toGetKlineReqBO(req);

        List<KLine> list = findKline(reqBO);

        return toGetKlineRespBO(list);
    }

    public void handleDepth(MatchResultBO event) {
        txTemplate.executeWithoutResult(transactionStatus -> {
            TickRecord tickRecord = buildTickRecord(event, EnumEventType.DEPTH);
            iTickRecordService.saveTickRecord(tickRecord);

            OrderItemBO takerOrder = event.getTakerOrder();
            HandicapHandler handler = HandicapHandler.getInstance(takerOrder.getMatchStatus());
            handler.handleHandicapUpdatedEvent(event);

            DepthDTO depthDTO = generateDepth(event.getCoinId());
            kafkaMQService.sendMessage(QuoteTopic.DEPTH_TOPIC, depthDTO);
        });

    }

    public void handleKLine(MatchResultBO event) {
        txTemplate.executeWithoutResult(transactionStatus -> {

            TickRecord tickRecord = buildTickRecord(event, EnumEventType.KLINE);
            iTickRecordService.saveTickRecord(tickRecord);

            List<KLine> kLines = ikLineService.handleKLineUpdatedEvent(event);
            List<KLineDTO> kLineDTOList = buildKLineDTOList(kLines);

            kafkaMQService.sendMessage(QuoteTopic.K_LINE_TOPIC, kLineDTOList);
        });

    }

    public void handleAggTrade(MatchResultBO event) {
        txTemplate.executeWithoutResult(transactionStatus -> {
            TickRecord tickRecord = buildTickRecord(event, EnumEventType.AGG_TRADE);
            iTickRecordService.saveTickRecord(tickRecord);

            AggTradeRecord aggTradeRecord = buildAggTradeRecord(event);
            iAggTradeRecordService.saveAggTradeRecord(aggTradeRecord);

            AggTradeRecordDTO aggTradeRecordDTO = buildAggTradeRecordDTO(aggTradeRecord);
            kafkaMQService.sendMessage(QuoteTopic.AGG_TRADE_TOPIC, aggTradeRecordDTO);
        });
    }

}
