package com.notayessir.user.user.constant;


import lombok.Getter;

@Getter
public enum EnumAccountOpType {



    FREEZE(10),

    UNFREEZE(20),

    ADD_UP_AVAILABLE(30),

    ADD_UP_HOLD(40),

    ;


    private final Integer code;


    EnumAccountOpType(Integer code) {
        this.code = code;
    }
}
