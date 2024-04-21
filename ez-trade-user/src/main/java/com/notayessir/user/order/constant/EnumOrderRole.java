package com.notayessir.user.order.constant;


import lombok.Getter;

@Getter
public enum EnumOrderRole {


    MAKER(10, "maker"),

    TAKER(20, "taker")



    ;


    private final Integer code;

    private final String roleName;

    EnumOrderRole(Integer code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }
}
