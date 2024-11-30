package com.notayessir.quote.api.spot.constant;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Getter
public enum EnumPricePrecision {

    /**
     * 0.01
     */
    P_0_01(new BigDecimal("0.01")),

    /**
     * 0.1
     */
    P_0_1(new BigDecimal("0.1")),

    /**
     * 1
     */
    P_1(new BigDecimal("1")),

    /**
     * 10
     */
    P_10(new BigDecimal("10")),

    P_50(new BigDecimal("50")),

    P_100(new BigDecimal("100")),

    ;


    private final BigDecimal precision;


    EnumPricePrecision(BigDecimal precision) {
        this.precision = precision;
    }


    public static EnumPricePrecision getPrecision(BigDecimal precision){
        for (EnumPricePrecision value : EnumPricePrecision.values()) {
            if (value.precision.compareTo(precision) == 0){
                return value;
            }
        }
        return null;
    }

}
