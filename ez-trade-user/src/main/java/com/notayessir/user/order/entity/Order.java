package com.notayessir.user.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author nobody
 * @since 2024-03-31
 */
@Getter
@Setter
@TableName("t_order")
public class Order implements Serializable {

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
}
