package com.common.common.constant;

import lombok.Getter;

@Getter
public enum EnumVersionField {


    ZERO(0)
    ;

    private final Integer code;


    EnumVersionField(Integer code) {
        this.code = code;
    }

}
