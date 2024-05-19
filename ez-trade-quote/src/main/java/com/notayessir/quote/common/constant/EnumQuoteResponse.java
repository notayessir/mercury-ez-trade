package com.notayessir.quote.common.constant;

import lombok.Getter;

@Getter
public enum EnumQuoteResponse {

    SUCCESS("e00000", "success"),

    ERROR("e00001", "error"),

    PARAM_BLANK("e00002", "param.blank"),



    ;

    EnumQuoteResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;

}
