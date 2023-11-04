package com.common.common.constant;


import lombok.Getter;

@Getter
public enum EnumDeletedField {

    DELETED(1),

    NOT_DELETED(0)
    ;

    private final Integer code;


    EnumDeletedField(Integer code) {
        this.code = code;
    }

}
