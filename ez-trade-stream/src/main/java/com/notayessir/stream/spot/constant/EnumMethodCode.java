package com.notayessir.stream.spot.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum EnumMethodCode {


    SUBSCRIBE("subscribe"),

    UNSUBSCRIBE("unsubscribe"),

    ;


    private final String code;

    EnumMethodCode(String code) {
        this.code = code;
    }

    public static EnumMethodCode getByMethodCode(String code){
        if (StringUtils.isBlank(code)){
            return null;
        }
        for (EnumMethodCode value : EnumMethodCode.values()) {
            if (StringUtils.equalsIgnoreCase(code, value.code)){
                return value;
            }
        }
        return null;

    }
}
