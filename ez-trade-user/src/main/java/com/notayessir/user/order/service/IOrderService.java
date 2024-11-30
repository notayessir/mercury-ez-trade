package com.notayessir.user.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.engine.api.bo.MatchItemBO;
import com.notayessir.user.order.bo.FindOrdersReqBO;
import com.notayessir.user.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService extends IService<Order> {

    Page<Order> findOrders(BasePageReq<FindOrdersReqBO> req);

    void createOrder(Order order);

    void cancelOrder(Order order);
    Order cancelOrder(Long orderId);

    void cancellingOrder(Order order);
    Order cancellingOrder(Long orderId);

    void submittedOrder(Order order);
    Order submittedOrder(Long orderId);

    void toFillOrder(Order order);
    Order toFillOrder(Long orderId);

    void fillingTakerOrder(Order order, BigDecimal qty, BigDecimal amount, List<MatchItemBO> trades);
    Order fillingTakerOrder(Long orderId, BigDecimal qty, BigDecimal amount, List<MatchItemBO> trades);

    void fillingMakerOrder(Order order, BigDecimal qty, BigDecimal price);
    Order fillingMakerOrder(Long orderId, BigDecimal qty, BigDecimal price);

    Order findOrder(Long orderId, Long userId);

}
