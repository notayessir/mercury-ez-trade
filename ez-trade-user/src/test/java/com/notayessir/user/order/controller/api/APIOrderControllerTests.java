package com.notayessir.user.order.controller.api;


import cn.hutool.core.util.RandomUtil;
import com.notayessir.common.vo.BusinessResp;
import com.notayessir.common.vo.BusinessRespCode;
import com.notayessir.user.api.order.constant.EnumEntrustProp;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.api.order.constant.EnumEntrustType;
import com.notayessir.user.order.vo.CreateOrderReq;
import com.notayessir.user.order.vo.CreateOrderResp;
import com.notayessir.user.order.vo.CancelOrderReq;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Objects;

@SpringBootTest(classes = APIOrderController.class)
public class APIOrderControllerTests {


    @Autowired
    private APIOrderController controller;


    @Test
    void contextLoads() throws Exception {
        Assumptions.assumeTrue(Objects.nonNull(controller));
    }


    @Test
    public void cancelOrderTest(){
        // limit order
        CreateOrderReq req = new CreateOrderReq();
        req.setUserId(1774333046012248064L);
        req.setCoinId(1774345784570761216L);
        req.setRequestId(RandomUtil.randomString(32));
        req.setSide(EnumEntrustSide.BUY.getSide());
        req.setBasePrice(new BigDecimal(100));
        req.setEntrustPrice(new BigDecimal(90));
        req.setEntrustQty(new BigDecimal(2));
        req.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        BusinessResp<CreateOrderResp> resp = controller.createOrder(req);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Long orderId = resp.getData().getOrderId();
        Assumptions.assumeTrue(Objects.nonNull(orderId));

        // cancel order
        CancelOrderReq req1 = new CancelOrderReq();
        req1.setOrderId(orderId);
        BusinessResp<Void> patchedOrderResp = controller.cancelOrder(req1);
        Assumptions.assumeTrue(StringUtils.equals(patchedOrderResp.getCode(), BusinessRespCode.SUCCESS.getCode()));
    }

    @Test
    public void createOrderTest(){
        // limit order
        CreateOrderReq req = new CreateOrderReq();
        req.setUserId(1774333046012248064L);
        req.setCoinId(1774345784570761216L);
        req.setRequestId(RandomUtil.randomString(32));
        req.setSide(EnumEntrustSide.BUY.getSide());
        req.setBasePrice(new BigDecimal(100));
        req.setEntrustPrice(new BigDecimal(90));
        req.setEntrustQty(new BigDecimal(2));
        req.setEntrustType(EnumEntrustType.NORMAL_LIMIT.getType());
        BusinessResp<CreateOrderResp> resp = controller.createOrder(req);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Assumptions.assumeTrue(Objects.nonNull(resp.getData().getOrderId()));

        // market order
        CreateOrderReq req1 = new CreateOrderReq();
        req1.setUserId(1774333046012248064L);
        req1.setCoinId(1774345784570761216L);
        req1.setRequestId(RandomUtil.randomString(32));
        req1.setSide(EnumEntrustSide.BUY.getSide());
        req1.setBasePrice(new BigDecimal(100));
        req1.setEntrustAmount(new BigDecimal(500));
        req1.setEntrustType(EnumEntrustType.MARKET.getType());
        resp = controller.createOrder(req1);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Assumptions.assumeTrue(Objects.nonNull(resp.getData().getOrderId()));


        // limit order
        CreateOrderReq req2 = new CreateOrderReq();
        req2.setUserId(1774333046012248064L);
        req2.setCoinId(1774345784570761216L);
        req2.setRequestId(RandomUtil.randomString(32));
        req2.setSide(EnumEntrustSide.BUY.getSide());
        req2.setBasePrice(new BigDecimal(100));
        req2.setEntrustPrice(new BigDecimal(90));
        req2.setEntrustQty(new BigDecimal(2));
        req2.setEntrustType(EnumEntrustType.PREMIUM_LIMIT.getType());
        req2.setEntrustProp(EnumEntrustProp.IOC.getType());
        resp = controller.createOrder(req2);
        Assumptions.assumeTrue(StringUtils.equals(resp.getCode(), BusinessRespCode.SUCCESS.getCode()));
        Assumptions.assumeTrue(Objects.nonNull(resp.getData().getOrderId()));
    }





}
