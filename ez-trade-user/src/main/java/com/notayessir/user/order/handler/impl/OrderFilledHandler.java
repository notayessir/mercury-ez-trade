package com.notayessir.user.order.handler.impl;

import com.notayessir.bo.MatchItemBO;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.bo.OrderItemBO;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.api.order.constant.EnumEntrustType;
import com.notayessir.user.order.constant.EnumOrderStatus;
import com.notayessir.user.order.entity.Order;
import com.notayessir.user.order.handler.OrderCommonHandler;
import com.notayessir.user.order.handler.OrderExecutedHandler;
import com.notayessir.user.order.service.IOrderService;
import com.notayessir.user.user.service.IAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class OrderFilledHandler implements OrderExecutedHandler {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IAccountService iAccountService;

    @Autowired
    private OrderCommonHandler orderCommonHandler;


    @Override
    public void handle(MatchResultBO event) {


        // 1. handle taker
        handleTaker(event);

        // 2. handle maker
        handleMaker(event);




    }

    private void handleMaker(MatchResultBO event) {
        List<MatchItemBO> matchItems = event.getMatchItems();
//        if (CollectionUtil.isEmpty(matchItems)){
//            return;
//        }

        for (MatchItemBO trade : matchItems) {
            // 1. update order
            OrderItemBO makerOrder = trade.getMakerOrder();
            Order order = iOrderService.getById(makerOrder.getOrderId());
            BigDecimal qty = trade.getClinchQty();
            BigDecimal price = trade.getClinchPrice();

            iOrderService.fillingMakerOrder(makerOrder.getOrderId(), qty, price);

            // 2. exchange asset
            BigDecimal amount = qty.multiply(price);
            String toDeductCurrency = order.getCoinBaseCurrency();
            String toAddCurrency = order.getCoinQuoteCurrency();
            BigDecimal toDeductQty = amount;
            BigDecimal toAddQty = qty;
            if (order.getEntrustSide().equals(EnumEntrustSide.SELL.getSide())){
                toDeductCurrency = order.getCoinQuoteCurrency();
                toAddCurrency = order.getCoinBaseCurrency();
                toDeductQty = qty;
                toAddQty = amount;
            }
            iAccountService.exchange(order.getUserId(), toDeductCurrency, toDeductQty, toAddCurrency, toAddQty);

            // 3. create capital record if filled
            if (order.getOrderStatus() == EnumOrderStatus.FILLED.getCode()){
                orderCommonHandler.createCapitalRecord(makerOrder);
            }
        }


    }

    private void handleTaker(MatchResultBO event) {
        BigDecimal qty = event.gatherClinchQty();
        BigDecimal amount = event.gatherClinchAmount();

        // 1. update order status
        OrderItemBO takerOrder = event.getTakerOrder();
        Order order = iOrderService.getById(takerOrder.getOrderId());
        List<MatchItemBO> trades = event.getMatchItems();
        iOrderService.fillingTakerOrder(order, qty, amount, trades);


        // 2. update asset
        String toDeductCurrency = order.getCoinBaseCurrency();
        String toAddCurrency = order.getCoinQuoteCurrency();
        BigDecimal toDeductQty = amount;
        BigDecimal toAddQty = qty;
        if (order.getEntrustSide().equals(EnumEntrustSide.SELL.getSide())){
            toDeductCurrency = order.getCoinQuoteCurrency();
            toAddCurrency = order.getCoinBaseCurrency();
            toDeductQty = qty;
            toAddQty = amount;
        }
        iAccountService.exchange(order.getUserId(), toDeductCurrency, toDeductQty, toAddCurrency, toAddQty);

        // 2.1 market order always come to final status, here need to release remain resource
        if (EnumEntrustType.MARKET.getType() == order.getEntrustType()){
            BigDecimal remainNum;
            if (StringUtils.equalsIgnoreCase(EnumEntrustSide.BUY.getSide(),order.getEntrustSide())){
                remainNum = takerOrder.getRemainEntrustAmount();
            } else {
                remainNum = takerOrder.getRemainEntrustQty();
            }
            if (remainNum.compareTo(BigDecimal.ZERO) > 0){
                // notice: release deduct currency
                iAccountService.addAvailable(order.getUserId(), toDeductCurrency, remainNum);
            }
        }

        // 3. create capital record if filled
        if (order.getOrderStatus() == EnumOrderStatus.FILLED.getCode()){
            orderCommonHandler.createCapitalRecord(takerOrder);
        }

    }



}
