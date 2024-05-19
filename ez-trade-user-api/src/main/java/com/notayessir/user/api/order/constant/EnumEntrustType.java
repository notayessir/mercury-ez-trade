package com.notayessir.user.api.order.constant;

import lombok.Getter;

@Getter
public enum EnumEntrustType {

    MARKET(1, "市价单"),

    NORMAL_LIMIT(2, "限价单"),

    PREMIUM_LIMIT(3, "高级限价单")

    ;


    private final int type;

    private final String desc;



    EnumEntrustType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static EnumEntrustType getByType(int type){
        EnumEntrustType[] values = EnumEntrustType.values();
        for (EnumEntrustType value : values) {
            if (value.type == type)
                return value;
        }
        return null;
    }

}
