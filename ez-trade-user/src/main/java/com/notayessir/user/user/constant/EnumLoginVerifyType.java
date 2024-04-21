package com.notayessir.user.user.constant;


import lombok.Getter;

@Getter
public enum EnumLoginVerifyType {

    PASSWORD(0),

    EMAIL_CODE(10)
    ;

    private final Integer code;

    EnumLoginVerifyType(Integer code) {
        this.code = code;
    }


    public static EnumLoginVerifyType getByCode(Integer code){
        EnumLoginVerifyType[] values = EnumLoginVerifyType.values();
        for (EnumLoginVerifyType value : values) {
            if (value.code.equals(code))
                return value;
        }
        return null;
    }

}
