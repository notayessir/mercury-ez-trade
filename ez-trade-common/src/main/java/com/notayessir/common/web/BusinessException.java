package com.notayessir.common.web;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    protected String code;

    protected String msg;

    protected Object[] args;


    public BusinessException(String msg) {
        super(msg);
    }



    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String code, String msg, Object[] args) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.args = args;
    }


}
