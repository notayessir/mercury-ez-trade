package com.notayessir.common.constant;


import lombok.Getter;

@Getter
public enum EnumFieldDeleted {

    DELETED(1),

    NOT_DELETED(0)
    ;

    private final Integer code;


    EnumFieldDeleted(Integer code) {
        this.code = code;
    }

}
