package com.notayessir.quote.spot.constant;

import lombok.Getter;

@Getter
public enum EnumEventType {


    AGG_TRADE("AGG_TRADE"),

    KLINE("KLINE"),

    DEPTH("DEPTH")



    ;


    private final  String type;

    EnumEventType(String type) {
        this.type = type;
    }
}
