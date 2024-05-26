package com.notayessir.queue.route.service;

import com.alibaba.fastjson2.JSONObject;
import com.notayessir.user.api.order.mq.OrderEvent;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class FacadeRouteServiceTests {


    @Autowired
    private FacadeRouteService facadeRouteService;


    @Test
    void contextLoads() throws Exception {
        Assumptions.assumeTrue(Objects.nonNull(facadeRouteService));
    }


    @Test
    public void handleOrderEventTest(){
        String text = "{\n" +
                "    \"baseScale\": 2,\n" +
                "    \"coinId\": 1774345784570761216,\n" +
                "    \"command\": 20,\n" +
                "    \"entrustAmount\": 4,\n" +
                "    \"entrustPrice\": 2,\n" +
                "    \"entrustQty\": 2,\n" +
                "    \"entrustSide\": \"BUY\",\n" +
                "    \"entrustType\": 2,\n" +
                "    \"orderId\": 1792055619693178880,\n" +
                "    \"quoteScale\": 18,\n" +
                "    \"requestId\": 1792055619693178880,\n" +
                "    \"timestamp\": 1716094329264,\n" +
                "    \"userId\": 1776256193975771136\n" +
                "}";
        OrderEvent orderEvent = JSONObject.parseObject(text, OrderEvent.class);
        facadeRouteService.handleOrderEvent(orderEvent);
    }

}
