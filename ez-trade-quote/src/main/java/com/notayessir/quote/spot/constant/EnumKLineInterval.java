package com.notayessir.quote.spot.constant;

import lombok.Getter;

@Getter
public enum EnumKLineInterval {

    ONE_SECOND(1, "1s"),
    ONE_MINUTE(60, "1min"),
    FIFTEEN_MINUTE(15 * 60, "15min"),
    THIRTY_MINUTE(30 * 60, "30min"),
    ONE_HOUR(60 * 60, "1hrs"),
    SIX_HOUR(60 * 60 * 6, "6hrs"),
    ONE_DAY(60 * 60 * 24, "1day"),

    ONE_WEEK(-1, "1week"),
    ONE_MONTH(-1, "1mon"),
    ONE_YEAR(-1, "1yrs")
    

    ;

    private final int value;
    private final String desc;

    EnumKLineInterval(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }







}
