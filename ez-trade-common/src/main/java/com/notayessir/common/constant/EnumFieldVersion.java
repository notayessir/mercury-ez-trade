package com.notayessir.common.constant;

import lombok.Getter;

@Getter
public enum EnumFieldVersion {


    INIT(1)
    ;

    private final Integer code;


    EnumFieldVersion(Integer code) {
        this.code = code;
    }

}
