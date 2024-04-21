package com.notayessir.common.constant;


import lombok.Getter;

@Getter
public enum EnumRequestSource {


    ADMIN(10),

    API(20),

    PUBLIC_API(22),

    INTERNAL(30)
    ;


    private final int source;

    EnumRequestSource(int source) {
        this.source = source;
    }
}
