package com.notayessir.gateway.vo;

import lombok.Getter;

@Getter
public enum EnumGatewayResponse {

    SUCCESS("e00000", "success"),

    ERROR("e00001", "error"),

    TOKEN_EXPIRE("e00002", "token.expired"),



    ;

    EnumGatewayResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;

}
