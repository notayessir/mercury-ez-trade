package com.notayessir.user.user.constant;


import lombok.Getter;

@Getter
public enum EnumCapitalDirection {


    FLOW_IN(10, "买入成交"),

    FLOW_OUT(20, "卖出成交")


    ;


    private final int code;
    private final String desc;

    EnumCapitalDirection(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
