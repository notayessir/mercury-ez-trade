package com.notayessir.user.order.handler.impl;

import com.notayessir.bo.MatchResultBO;
import com.notayessir.bo.OrderItemBO;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.order.entity.Order;
import com.notayessir.user.order.handler.OrderCommonHandler;
import com.notayessir.user.order.handler.OrderExecutedHandler;
import com.notayessir.user.order.service.IOrderService;
import com.notayessir.user.user.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class OrderCancelledHandler implements OrderExecutedHandler {


    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IAccountService iAccountService;

    @Autowired
    private OrderCommonHandler orderCommonHandler;

    @Override
    public void handle(MatchResultBO event) {
//        Integer commandType = event.getCommandType();
//        if (!commandType.equals(EnumMatchCommand.CANCEL.getCode())){
//            return;
//        }
        OrderItemBO takerOrder = event.getTakerOrder();
        // 1. cancel order
        cancelOrder(takerOrder);

        // 2. unfreeze asset
        unfreezeAsset(takerOrder);

        // 3. create a capital record
        orderCommonHandler.createCapitalRecord(takerOrder);
    }



    private void unfreezeAsset(OrderItemBO takerOrder) {
        Order order = iOrderService.getById(takerOrder.getOrderId());
        String currency = order.getCoinBaseCurrency();
        BigDecimal remainQty = takerOrder.getRemainEntrustAmount();
        if (order.getEntrustSide().equals(EnumEntrustSide.SELL.getSide())){
            currency = order.getCoinQuoteCurrency();
            remainQty = takerOrder.getRemainEntrustQty();
        }

        iAccountService.moveHoldToAvailable(order.getUserId(), currency, remainQty);
    }

    private void cancelOrder(OrderItemBO takerOrder) {
        Order order = iOrderService.cancelOrder(takerOrder.getOrderId());
    }


}
