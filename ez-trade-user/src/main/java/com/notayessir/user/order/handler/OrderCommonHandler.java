package com.notayessir.user.order.handler;

import com.notayessir.bo.OrderItemBO;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.order.entity.Order;
import com.notayessir.user.order.service.IOrderService;
import com.notayessir.user.user.constant.EnumCapitalBusinessCode;
import com.notayessir.user.user.service.ICapitalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCommonHandler {


    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private ICapitalRecordService iCapitalRecordService;

    public void createCapitalRecord(OrderItemBO takerOrder) {
        if (takerOrder.getEntrustSide() == EnumEntrustSide.SELL.getCode()){
            if (takerOrder.getEntrustQty().compareTo(takerOrder.getRemainEntrustQty()) == 0 ){
                return;
            }
            Long orderId = takerOrder.getOrderId();
            Order order = iOrderService.getById(orderId);

            iCapitalRecordService.createCapitalFlowIn(order.getUserId(), order.getCoinBaseCurrency(), order.getClinchAmount(), EnumCapitalBusinessCode.SELL);
            iCapitalRecordService.createCapitalFlowOut(order.getUserId(), order.getCoinQuoteCurrency(), order.getClinchQty(), EnumCapitalBusinessCode.SELL);

        } else {
            // BUY
            if (takerOrder.getEntrustAmount().compareTo(takerOrder.getRemainEntrustAmount()) == 0 ){
                return;
            }
            Long orderId = takerOrder.getOrderId();
            Order order = iOrderService.getById(orderId);
            iCapitalRecordService.createCapitalFlowIn(order.getUserId(), order.getCoinQuoteCurrency(), order.getClinchQty(), EnumCapitalBusinessCode.BUY);
            iCapitalRecordService.createCapitalFlowOut(order.getUserId(), order.getCoinBaseCurrency(), order.getClinchAmount(), EnumCapitalBusinessCode.BUY);
        }
    }

}
