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
}
