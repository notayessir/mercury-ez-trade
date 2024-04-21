package com.notayessir.quote.service;

import cn.hutool.core.util.IdUtil;
import com.notayessir.bo.MatchItemBO;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.bo.OrderItemBO;
import com.notayessir.constant.EnumEntrustSide;
import com.notayessir.constant.EnumEntrustType;
import com.notayessir.constant.EnumMatchStatus;
import com.notayessir.quote.spot.service.FacadeSpotQuoteService;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest
public class FacadeSpotQuoteServiceTests {

    @Autowired
    private FacadeSpotQuoteService facadeSpotQuoteService;


    @Test
    void contextLoads() throws Exception {
        Assumptions.assumeTrue(Objects.nonNull(facadeSpotQuoteService));
    }

    @Test
    public void handleQuoteUpdateEventTest(){
        // a new buy order
        Long coinId = 1774345784570761216L;
        Long sellOrderId = IdUtil.getSnowflakeNextId();
        OrderItemBO takerOrder = new OrderItemBO();
        takerOrder.setOrderId(sellOrderId);
        takerOrder.setEntrustPrice(new BigDecimal("90.4544"));
        takerOrder.setEntrustQty(new BigDecimal(2));
        takerOrder.setRemainEntrustAmount(takerOrder.getEntrustQty().multiply(takerOrder.getEntrustPrice()));
        takerOrder.setRemainEntrustQty(takerOrder.getEntrustQty());
        takerOrder.setEntrustAmount(takerOrder.getEntrustQty().multiply(takerOrder.getEntrustPrice()));
        takerOrder.setEntrustSide(EnumEntrustSide.SELL.getCode());
        takerOrder.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        takerOrder.setMatchStatus(EnumMatchStatus.NEW.getStatus());
        takerOrder.setBaseScale(4);
        takerOrder.setQuoteScale(4);
        MatchResultBO event = new MatchResultBO();
        event.setGlobalSequence(IdUtil.getSnowflakeNextId());
        event.setTxSequence(IdUtil.getSnowflakeNextId());
        event.setTimestamp(System.currentTimeMillis());
        event.setCoinId(coinId);
        event.setTakerOrder(takerOrder);
        facadeSpotQuoteService.handleQuoteUpdateEvent(event);


        // a new buy order

        Long buyOrderId = IdUtil.getSnowflakeNextId();
        OrderItemBO takerOrder1 = new OrderItemBO();
        takerOrder1.setOrderId(buyOrderId);
        takerOrder1.setEntrustPrice(new BigDecimal("90.4544"));
        takerOrder1.setEntrustQty(new BigDecimal(2));
        takerOrder1.setRemainEntrustAmount(takerOrder1.getEntrustQty().multiply(takerOrder1.getEntrustPrice()));
        takerOrder1.setRemainEntrustQty(takerOrder1.getEntrustQty());
        takerOrder1.setEntrustAmount(takerOrder1.getEntrustQty().multiply(takerOrder1.getEntrustPrice()));
        takerOrder1.setEntrustSide(EnumEntrustSide.BUY.getCode());
        takerOrder1.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        takerOrder1.setMatchStatus(EnumMatchStatus.NEW.getStatus());
        takerOrder1.setBaseScale(4);
        takerOrder1.setQuoteScale(4);
        MatchResultBO event1 = new MatchResultBO();
        event1.setGlobalSequence(IdUtil.getSnowflakeNextId());
        event1.setTxSequence(IdUtil.getSnowflakeNextId());
        event1.setTimestamp(System.currentTimeMillis());
        event1.setCoinId(coinId);
        event1.setTakerOrder(takerOrder1);
        facadeSpotQuoteService.handleQuoteUpdateEvent(event1);
    }
}
