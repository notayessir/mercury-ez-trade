package com.common.common.exception;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BusinessException extends RuntimeException {

    private String code;

    private String msg;

    private Object[] args;


    public BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public BusinessException(String code, String msg, Object[] args) {
        this.code = code;
        this.msg = msg;
        this.args = args;
    }


}
