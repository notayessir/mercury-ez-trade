package com.notayessir.user.user.constant;


import lombok.Getter;

@Getter
public enum EnumCapitalBusinessCode {


    BUY(10, "买入成交"),

    SELL(20, "卖出成交"),

    DEPOSIT(30, "入金"),


    ;


    private final int code;
    private final String desc;

    EnumCapitalBusinessCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EnumCapitalBusinessCode getByCode(int code){
        EnumCapitalBusinessCode[] values = EnumCapitalBusinessCode.values();
        for (EnumCapitalBusinessCode value : values) {
            if (value.code == code)
                return value;
        }
        return null;
    }


}
