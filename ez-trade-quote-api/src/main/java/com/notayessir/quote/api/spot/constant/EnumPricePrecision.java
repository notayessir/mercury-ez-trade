package com.notayessir.quote.api.spot.constant;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * price.divide(x, scale, RoundingMode.DOWN);
 */
@Getter
public enum EnumPricePrecision {

    /**
     * 0.01
     */
    FRACTION_0_01( new BigDecimal("0.01")),

    /**
     * 0.1
     */
    FRACTION_0_1(new BigDecimal("0.1")),

    /**
     * 1
     */
    ROUND_1(BigDecimal.ONE),

    /**
     * 10
     */
    ROUND_10(BigDecimal.TEN),

    ROUND_50(new BigDecimal("50")),


    ;


    private final BigDecimal precision;


    EnumPricePrecision( BigDecimal precision) {
        this.precision = precision;
    }

}
