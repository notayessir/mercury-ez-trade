package com.notayessir.user.order.service;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.notayessir.bo.MatchItemBO;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.bo.OrderItemBO;
import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.BusinessRespCode;
import com.notayessir.constant.EnumMatchStatus;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.api.order.constant.EnumEntrustType;
import com.notayessir.user.order.controller.api.APIOrderController;
import com.notayessir.user.order.vo.CreateOrderReq;
import com.notayessir.user.order.vo.CreateOrderResp;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = FacadeOrderService.class)
public class FacadeOrderServiceTests {


    @Autowired
    private FacadeOrderService facadeOrderService;
    @Autowired
    private APIOrderController controller;


    @Test
    void contextLoads() throws Exception {
        Assumptions.assumeTrue(Objects.nonNull(controller));

        Assumptions.assumeTrue(Objects.nonNull(facadeOrderService));
    }


    @DisplayName("filled order")
    @Test
    public void test1(){
        Long coinId = 1774345784570761216L;

        // submit a sell order
        CreateOrderReq sellReq = new CreateOrderReq();
        sellReq.setUserId(1775787718073987072L);
        sellReq.setCoinId(coinId);
        sellReq.setRequestId(RandomUtil.randomString(32));
        sellReq.setSide(EnumEntrustSide.SELL.getSide());
        sellReq.setBasePrice(new BigDecimal(100));
        sellReq.setEntrustPrice(new BigDecimal(90));
        sellReq.setEntrustQty(new BigDecimal(2));
        sellReq.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        BusinessResp<CreateOrderResp> resp = controller.createOrder(sellReq);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Long sellOrderId = resp.getData().getOrderId();

        // submit a buy order
        CreateOrderReq buyReq = new CreateOrderReq();
        buyReq.setUserId(1774333046012248064L);
        buyReq.setCoinId(coinId);
        buyReq.setRequestId(RandomUtil.randomString(32));
        buyReq.setSide(EnumEntrustSide.BUY.getSide());
        buyReq.setBasePrice(new BigDecimal(100));
        buyReq.setEntrustPrice(new BigDecimal(90));
        buyReq.setEntrustQty(new BigDecimal(2));
        buyReq.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        resp = controller.createOrder(buyReq);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Long buyOrderId = resp.getData().getOrderId();


        // mock two order matched
        // taker order
        OrderItemBO takerOrder = new OrderItemBO();
        takerOrder.setOrderId(buyOrderId);
        takerOrder.setEntrustPrice(new BigDecimal(90));
        takerOrder.setEntrustQty(new BigDecimal(2));
        takerOrder.setRemainEntrustAmount(BigDecimal.ZERO);
        takerOrder.setRemainEntrustQty(BigDecimal.ZERO);
        takerOrder.setEntrustAmount(takerOrder.getEntrustQty().multiply(takerOrder.getEntrustPrice()));
        takerOrder.setEntrustSide(EnumEntrustSide.SELL.getCode());
        takerOrder.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        takerOrder.setMatchStatus(EnumMatchStatus.FILLED.getStatus());

        List<MatchItemBO> matchItems = new ArrayList<>();
        MatchItemBO matchItemBO = new MatchItemBO();
        matchItemBO.setClinchPrice(new BigDecimal(90));
        matchItemBO.setClinchQty(new BigDecimal(2));
        matchItemBO.setSequence(0L);

        // maker order
        OrderItemBO makerOrder = new OrderItemBO();
        makerOrder.setOrderId(sellOrderId);
        makerOrder.setEntrustPrice(new BigDecimal(90));
        makerOrder.setEntrustQty(new BigDecimal(2));
        makerOrder.setRemainEntrustAmount(BigDecimal.ZERO);
        makerOrder.setRemainEntrustQty(BigDecimal.ZERO);
        makerOrder.setEntrustAmount(takerOrder.getEntrustQty().multiply(takerOrder.getEntrustPrice()));
        makerOrder.setEntrustSide(EnumEntrustSide.BUY.getCode());
        makerOrder.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        makerOrder.setMatchStatus(EnumMatchStatus.FILLED.getStatus());

        matchItemBO.setMakerOrder(makerOrder);
        matchItems.add(matchItemBO);

        MatchResultBO event = new MatchResultBO();
        event.setGlobalSequence(IdUtil.getSnowflakeNextId());
        event.setTxSequence(IdUtil.getSnowflakeNextId());
        event.setTimestamp(System.currentTimeMillis());
        event.setCoinId(coinId);
        event.setTakerOrder(takerOrder);
        event.setMatchItems(matchItems);

        facadeOrderService.handleOrderExecutedEvent(event);
    }


    @DisplayName("partially filled order")
    @Test
    public void test2(){
        Long coinId = 1774345784570761216L;

        // submit a sell order, as maker1, adam
        CreateOrderReq sellReq = new CreateOrderReq();
        sellReq.setUserId(1775787718073987072L);
        sellReq.setCoinId(coinId);
        sellReq.setRequestId(RandomUtil.randomString(32));
        sellReq.setSide(EnumEntrustSide.SELL.getSide());
        sellReq.setBasePrice(new BigDecimal(100));
        sellReq.setEntrustPrice(new BigDecimal(90));
        sellReq.setEntrustQty(new BigDecimal(1));
        sellReq.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        BusinessResp<CreateOrderResp> resp = controller.createOrder(sellReq);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Long sellOrderId = resp.getData().getOrderId();


        // submit a sell order, as maker2, john
        CreateOrderReq sellReq1 = new CreateOrderReq();
        sellReq1.setUserId(1776256193975771136L);
        sellReq1.setCoinId(coinId);
        sellReq1.setRequestId(RandomUtil.randomString(32));
        sellReq1.setSide(EnumEntrustSide.SELL.getSide());
        sellReq1.setBasePrice(new BigDecimal(100));
        sellReq1.setEntrustPrice(new BigDecimal(90));
        sellReq1.setEntrustQty(new BigDecimal(1));
        sellReq1.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        resp = controller.createOrder(sellReq1);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Long sellOrderId1 = resp.getData().getOrderId();

        // submit a buy order, as taker, eddie
        CreateOrderReq buyReq = new CreateOrderReq();
        buyReq.setUserId(1774333046012248064L);
        buyReq.setCoinId(coinId);
        buyReq.setRequestId(RandomUtil.randomString(32));
        buyReq.setSide(EnumEntrustSide.BUY.getSide());
        buyReq.setBasePrice(new BigDecimal(100));
        buyReq.setEntrustPrice(new BigDecimal(90));
        buyReq.setEntrustQty(new BigDecimal(2));
        buyReq.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        resp = controller.createOrder(buyReq);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Long buyOrderId = resp.getData().getOrderId();


        // mock two order matched
        // taker order
        OrderItemBO takerOrder = new OrderItemBO();
        takerOrder.setOrderId(buyOrderId);
        takerOrder.setEntrustPrice(new BigDecimal(90));
        takerOrder.setEntrustQty(new BigDecimal(2));
        takerOrder.setRemainEntrustAmount(BigDecimal.ZERO);
        takerOrder.setRemainEntrustQty(BigDecimal.ZERO);
        takerOrder.setEntrustAmount(takerOrder.getEntrustQty().multiply(takerOrder.getEntrustPrice()));
        takerOrder.setEntrustSide(EnumEntrustSide.SELL.getCode());
        takerOrder.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        takerOrder.setMatchStatus(EnumMatchStatus.FILLED.getStatus());

        List<MatchItemBO> matchItems = new ArrayList<>();
        // maker1 order
        MatchItemBO matchItemBO = new MatchItemBO();
        matchItemBO.setClinchPrice(new BigDecimal(90));
        matchItemBO.setClinchQty(new BigDecimal(1));
        matchItemBO.setSequence(0L);
        OrderItemBO makerOrder = new OrderItemBO();
        makerOrder.setOrderId(sellOrderId);
        makerOrder.setEntrustPrice(new BigDecimal(90));
        makerOrder.setEntrustQty(new BigDecimal(1));
        makerOrder.setRemainEntrustAmount(BigDecimal.ZERO);
        makerOrder.setRemainEntrustQty(BigDecimal.ZERO);
        makerOrder.setEntrustAmount(takerOrder.getEntrustQty().multiply(takerOrder.getEntrustPrice()));
        makerOrder.setEntrustSide(EnumEntrustSide.BUY.getCode());
        makerOrder.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        makerOrder.setMatchStatus(EnumMatchStatus.FILLED.getStatus());
        matchItemBO.setMakerOrder(makerOrder);
        matchItems.add(matchItemBO);

        // maker2 order
        MatchItemBO matchItemBO1 = new MatchItemBO();
        matchItemBO1.setClinchPrice(new BigDecimal(90));
        matchItemBO1.setClinchQty(new BigDecimal(1));
        matchItemBO1.setSequence(1L);
        OrderItemBO makerOrder1 = new OrderItemBO();
        makerOrder1.setOrderId(sellOrderId1);
        makerOrder1.setEntrustPrice(new BigDecimal(90));
        makerOrder1.setEntrustQty(new BigDecimal(1));
        makerOrder1.setRemainEntrustAmount(BigDecimal.ZERO);
        makerOrder1.setRemainEntrustQty(BigDecimal.ZERO);
        makerOrder1.setEntrustAmount(takerOrder.getEntrustQty().multiply(takerOrder.getEntrustPrice()));
        makerOrder1.setEntrustSide(EnumEntrustSide.BUY.getCode());
        makerOrder1.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        makerOrder1.setMatchStatus(EnumMatchStatus.FILLED.getStatus());
        matchItemBO1.setMakerOrder(makerOrder1);
        matchItems.add(matchItemBO1);





        MatchResultBO event = new MatchResultBO();
        event.setGlobalSequence(IdUtil.getSnowflakeNextId());
        event.setTxSequence(IdUtil.getSnowflakeNextId());
        event.setTimestamp(System.currentTimeMillis());
        event.setCoinId(coinId);
        event.setTakerOrder(takerOrder);
        event.setMatchItems(matchItems);

        facadeOrderService.handleOrderExecutedEvent(event);
    }





}
