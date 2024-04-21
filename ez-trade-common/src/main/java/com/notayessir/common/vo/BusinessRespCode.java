package com.notayessir.common.vo;

import lombok.Getter;

@Getter
public enum BusinessRespCode {

    SUCCESS("e00000", "success"),

    ERROR("e00001", "server error"),


    ;

    BusinessRespCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;

}
