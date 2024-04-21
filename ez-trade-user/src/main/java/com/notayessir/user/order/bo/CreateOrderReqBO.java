package com.notayessir.user.order.bo;

import com.notayessir.common.vo.req.BaseReq;
import com.notayessir.user.coin.bo.CheckCoinParamBO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class CreateOrderReqBO extends BaseReq {



    /**
     * coin id
     */
    private Long coinId;


    /**
     * buy or sell
     */
    private String side;

    /**
     * coin price at the moment
     */
    private BigDecimal basePrice;

    /**
     * entrust qty
     */
    private BigDecimal entrustQty;

    /**
     * entrust price
     */
    private BigDecimal entrustPrice;


    /**
     * entrust amount
     */
    private BigDecimal entrustAmount;


    private Integer entrustProp;

    private Integer entrustType;



}
