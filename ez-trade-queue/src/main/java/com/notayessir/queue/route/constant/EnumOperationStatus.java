package com.notayessir.queue.route.constant;


import lombok.Getter;

@Getter
public enum EnumOperationStatus {


    DONE(1),


    UNDONE(0),





    ;


     EnumOperationStatus(Integer code) {
        this.code = code;
    }

    private final int code;

}
