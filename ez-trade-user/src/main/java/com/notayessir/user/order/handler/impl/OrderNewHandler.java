package com.notayessir.user.order.handler.impl;

import com.notayessir.engine.api.bo.MatchResultBO;
import com.notayessir.engine.api.bo.OrderItemBO;
import com.notayessir.user.order.handler.OrderExecutedHandler;
import com.notayessir.user.order.service.IOrderService;
import com.notayessir.user.user.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderNewHandler implements OrderExecutedHandler {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IAccountService iAccountService;


    @Override
    public void handle(MatchResultBO event) {
        OrderItemBO takerOrder = event.getTakerOrder();
        // 1. simply update order status
        iOrderService.toFillOrder(takerOrder.getOrderId());
    }








}
