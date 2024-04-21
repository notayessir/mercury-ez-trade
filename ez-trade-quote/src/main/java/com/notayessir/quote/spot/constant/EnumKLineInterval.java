package com.notayessir.quote.spot.constant;

import lombok.Getter;

@Getter
public enum EnumKLineInterval {
    
    
    ONE_MINUTE(1),

    FIFTEEN_MINUTE(15),
    
    HALF_HOUR(30),
    
    ONE_HOUR(60),

    SIX_HOUR(60 * 6),

    ONE_DAY(60 * 24),

    ONE_WEEK(-1),
    
    ONE_MONTH(-1),

    ONE_YEAR(-1)
    
    
    
    
    ;

    private final int value;

    EnumKLineInterval(int value) {
        this.value = value;
    }







}
