package com.notayessir.user.api.order.mq;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@Builder
public class OrderEvent implements Serializable {

    private Integer command;

    private Long coinId;

    private Long orderId;

    private Integer entrustProp;

    private Integer entrustType;

    private String entrustSide;

    /**
     * entrust unit price
     */
    private BigDecimal entrustPrice;

    /**
     * entrust number
     */
    private BigDecimal entrustQty;

    private BigDecimal entrustAmount;

    private int quoteScale;
    private int baseScale;

    /**
     * seem as request id
     */
    private Long requestId;

    private Long timestamp;

    private Long userId;

}
