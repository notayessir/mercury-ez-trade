package com.notayessir.user.order.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    private Long userId;

    private Long accountId;

    private String entrustSide;

    private String entrustQty;

    private String entrustPrice;

    private String entrustAmount;

    private String entrustProp;

    private Integer entrustType;


    private Long coinId;

    private String coinBaseCurrency;

    private String coinQuoteCurrency;

    private Integer deleted;

    private Integer orderStatus;

    private String clinchQty;

    private String clinchAmount;

    private Integer version;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDate tradeDate;

    private String matchSequence;

    private String orderStatusDesc;
    private String entrustTypeDesc;
}
