package com.notayessir.user.api.order.constant;


import lombok.Getter;

import java.util.Objects;

@Getter
public enum EnumEntrustProp {


    FOK(1),

    IOC(2)
    ;


    EnumEntrustProp(int type) {
        this.type = type;
    }

    private final int type;

    public static EnumEntrustProp getByType(Integer type){
        if (Objects.isNull(type)){
            return null;
        }
        EnumEntrustProp[] values = EnumEntrustProp.values();
        for (EnumEntrustProp value : values) {
            if (value.type == type)
                return value;
        }
        return null;
    }


}
