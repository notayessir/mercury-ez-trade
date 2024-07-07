package com.notayessir.user.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notayessir.bo.MatchItemBO;
import com.notayessir.common.constant.EnumFieldVersion;
import com.notayessir.common.vo.req.BasePageReq;
import com.notayessir.common.web.BusinessException;
import com.notayessir.user.api.order.constant.EnumEntrustSide;
import com.notayessir.user.api.order.constant.EnumEntrustType;
import com.notayessir.user.order.bo.FindOrdersReqBO;
import com.notayessir.user.order.constant.EnumOrderRole;
import com.notayessir.user.order.constant.EnumOrderStatus;
import com.notayessir.user.order.entity.ClinchRecord;
import com.notayessir.user.order.entity.Order;
import com.notayessir.user.order.mapper.OrderMapper;
import com.notayessir.user.order.service.IClinchRecordService;
import com.notayessir.user.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notayessir.user.common.constant.EnumUserResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {



    @Autowired
    private IClinchRecordService iClinchRecordService;


    @Override
    public Page<Order> findOrders(BasePageReq<FindOrdersReqBO> req) {
        Page<Order> iPage = Page.of(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, req.getUserId())
                .orderByDesc(Order::getId);
        return this.page(iPage, queryWrapper);
    }


    @Override
    public void createOrder(Order order) {
        // create order
        save(order);

    }


    private boolean updateOrder(Order order) {
        int curVersion = order.getVersion();
        order.setVersion(curVersion + 1);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getVersion, curVersion)
                .eq(Order::getId, order.getId());
        return this.update(order, queryWrapper);
    }

    @Override
    public Order cancelOrder(Long orderId) {
        Order order = getById(orderId);
        cancelOrder(order);
        return order;
    }

    @Override
    public void cancelOrder(Order order) {
        EnumOrderStatus status = EnumOrderStatus.CANCELLED;
        BigDecimal clinchQty = new BigDecimal(order.getClinchQty());
        if (clinchQty.compareTo(BigDecimal.ZERO) > 0){
            status = EnumOrderStatus.PARTIAL_FILLED_AND_CANCEL;
        }
        order.setOrderStatus(status.getCode());
        order.setUpdateTime(LocalDateTime.now());
        // update order
        boolean success = updateOrder(order);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }
    }




    @Override
    public Order cancellingOrder(Long orderId) {
        Order order = getById(orderId);
        cancellingOrder(order);
        return order;
    }

    @Override
    public void cancellingOrder(Order order) {
        order.setOrderStatus(EnumOrderStatus.CANCELLING.getCode());
        order.setUpdateTime(LocalDateTime.now());
        // update order
        boolean success = updateOrder(order);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }
    }

    @Override
    public Order submittedOrder(Long orderId) {
        Order order = getById(orderId);
        submittedOrder(order);
        return order;
    }

    @Override
    public void submittedOrder(Order order) {
        order.setOrderStatus(EnumOrderStatus.SUBMITTED.getCode());
        order.setUpdateTime(LocalDateTime.now());
        // update order
        boolean success = updateOrder(order);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }
    }


    @Override
    public Order toFillOrder(Long orderId) {
        Order order = getById(orderId);
        toFillOrder(order);
        return order;
    }

    @Override
    public void toFillOrder(Order order) {
        order.setOrderStatus(EnumOrderStatus.TO_FILL.getCode());
        order.setUpdateTime(LocalDateTime.now());
        // update order
        boolean success = updateOrder(order);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }
    }

    @Override
    public void fillingTakerOrder(Order order, BigDecimal qty, BigDecimal amount, List<MatchItemBO> trades) {
        BigDecimal entrustQty = new BigDecimal(order.getEntrustQty());
        BigDecimal clinchQty = new BigDecimal(order.getClinchQty());
        clinchQty = clinchQty.add(qty);
        order.setClinchQty(clinchQty.toString());

        BigDecimal clinchedAmount = new BigDecimal(order.getClinchAmount());
        clinchedAmount = clinchedAmount.add(amount);
        order.setClinchAmount(clinchedAmount.toString());
        EnumOrderStatus status = EnumOrderStatus.PARTIAL_FILLED;
        if (entrustQty.compareTo(clinchQty) == 0){
            status = EnumOrderStatus.FILLED;
        }

        // market order always end with 'filled' status
        if (EnumEntrustType.MARKET.getType() == order.getEntrustType()){
            status = EnumOrderStatus.FILLED;
        }

        order.setOrderStatus(status.getCode());
        order.setUpdateTime(LocalDateTime.now());
        // update order
        boolean success = updateOrder(order);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }

        for (MatchItemBO matchItemBO : trades) {
            ClinchRecord clinchRecord = buildClinchRecord(order, matchItemBO.getClinchQty(), matchItemBO.getClinchPrice(), EnumOrderRole.TAKER);
            iClinchRecordService.save(clinchRecord);
        }
    }

    private ClinchRecord buildClinchRecord(Order order, BigDecimal qty, BigDecimal price, EnumOrderRole role) {
        ClinchRecord clinchRecord = new ClinchRecord();
        clinchRecord.setId(IdUtil.getSnowflakeNextId());
        clinchRecord.setOrderId(order.getId());
        clinchRecord.setUserId(order.getUserId());
        clinchRecord.setCoinId(order.getCoinId());
        clinchRecord.setTradeDate(order.getTradeDate());

        clinchRecord.setQty(qty.toString());
        clinchRecord.setPrice(price.toString());
        clinchRecord.setRole(role.getRoleName());
        clinchRecord.setVersion(EnumFieldVersion.INIT.getCode());
        LocalDateTime now = LocalDateTime.now();
        clinchRecord.setCreateTime(now);
        clinchRecord.setUpdateTime(now);
        return clinchRecord;
    }

    @Override
    public Order fillingTakerOrder(Long orderId, BigDecimal toAddQty, BigDecimal toAddAmount, List<MatchItemBO> trades) {
        Order order = getById(orderId);
        fillingTakerOrder(order, toAddQty, toAddAmount, trades);
        return order;
    }

    @Override
    public void fillingMakerOrder(Order makerOrder, BigDecimal qty, BigDecimal price) {
        BigDecimal entrustQty = new BigDecimal(makerOrder.getEntrustQty());
        BigDecimal clinchQty = new BigDecimal(makerOrder.getClinchQty());
        clinchQty = clinchQty.add(qty);
        makerOrder.setClinchQty(clinchQty.toString());


        BigDecimal amount = new BigDecimal(makerOrder.getClinchAmount());
        amount = amount.add(qty.multiply(price));
        makerOrder.setClinchAmount(amount.toString());
        EnumOrderStatus status = EnumOrderStatus.PARTIAL_FILLED;
        if (entrustQty.compareTo(clinchQty) == 0){
            status = EnumOrderStatus.FILLED;
        }
        makerOrder.setOrderStatus(status.getCode());
        makerOrder.setUpdateTime(LocalDateTime.now());
        // update order
        boolean success = updateOrder(makerOrder);
        if (!success){
            throw new BusinessException(EnumUserResponse.VERSION_OUTDATED.getCode(), EnumUserResponse.VERSION_OUTDATED.getMessage());
        }
        ClinchRecord clinchRecord = buildClinchRecord(makerOrder, qty, price, EnumOrderRole.MAKER);
        iClinchRecordService.save(clinchRecord);
    }

    @Override
    public Order fillingMakerOrder(Long orderId, BigDecimal qty, BigDecimal price) {
        Order order = getById(orderId);
        fillingMakerOrder(order, qty, price);
        return order;
    }


    @Override
    public Order findOrder(Long orderId, Long userId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getId, orderId)
                .eq(Order::getUserId, userId);
        return getOne(queryWrapper);
    }


}
