package com.notayessir.engine.api.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
public class MatchItemBO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6120849803225177460L;

    private BigDecimal clinchQty;
//    private BigDecimal clinchAmount;
    private BigDecimal clinchPrice;


    private Long sequence;

    private OrderItemBO makerOrder;




}
