package com.notayessir.user.coin.bo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CheckCoinParamBO {


    private Long coinId;

    /**
     * coin price at the moment
     */
    private BigDecimal basePrice;

    /**
     * number of coin
     */
    private BigDecimal entrustQty;

    /**
     * entrust price
     */
    private BigDecimal entrustPrice;

    /**
     * trigger price
     */
    private BigDecimal triggerPrice;

    /**
     * clinch price
     */
    private BigDecimal entrustAmount;

    private Integer orderType;

}
