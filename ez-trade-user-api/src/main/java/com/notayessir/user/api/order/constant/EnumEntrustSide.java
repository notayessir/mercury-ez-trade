package com.notayessir.user.api.order.constant;

import lombok.Getter;

@Getter
public enum EnumEntrustSide {

    BUY("BUY", 1),

    SELL("SELL", 0)
    ;

    private final String side;

    private final int code;


    EnumEntrustSide(String side, int code) {
        this.side = side;
        this.code = code;
    }

    public static EnumEntrustSide getEntrustSide(String side){
        EnumEntrustSide[] values = EnumEntrustSide.values();
        for (EnumEntrustSide value : values) {
            if (side.equalsIgnoreCase(value.side)){
                return value;
            }
        }
        return null;
    }

    public static EnumEntrustSide getEntrustSide(int code){
        EnumEntrustSide[] values = EnumEntrustSide.values();
        for (EnumEntrustSide value : values) {
            if (code == value.code){
                return value;
            }
        }
        return null;
    }
}
