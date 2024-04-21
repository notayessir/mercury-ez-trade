package com.notayessir.common.vo;


import com.notayessir.common.util.MessageUtil;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BusinessResp<D> {

    private String code;

    private String message;

    private D data;

    public BusinessResp() {
    }

    public BusinessResp(String code, String message, D data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BusinessResp(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static BusinessResp<Void> ok(){
        String code = BusinessRespCode.SUCCESS.getCode();
        String message = MessageUtil.getMessage(BusinessRespCode.SUCCESS.getMessage());
        return new BusinessResp<>(code, message);
    }

    public static BusinessResp<Void> normal(String code, String messageKey){
        String message = MessageUtil.getMessage(messageKey);
        return new BusinessResp<>(code, message);
    }

    public static <D> BusinessResp<D> normal(String code, String messageKey, D data){
        String message = MessageUtil.getMessage(messageKey);
        return new BusinessResp<>(code, message, data);
    }

    public static <D> BusinessResp<D> ok(D data){
        String code = BusinessRespCode.SUCCESS.getCode();
        String message = MessageUtil.getMessage(BusinessRespCode.SUCCESS.getMessage());
        return new BusinessResp<>(code, message, data);

    }



    public static <D> BusinessResp<D> error(){
        String code = BusinessRespCode.ERROR.getCode();
        String message = MessageUtil.getMessage(BusinessRespCode.ERROR.getMessage());
        return new BusinessResp<>(code, message);
    }


    public static BusinessResp<?> error(String code, String messageKey, Object[] args){
        String message = MessageUtil.getMessage(messageKey, args);
        return new BusinessResp<>(code, message);
    }

    public static BusinessResp<Void> error(String code, String messageKey){
        String message = MessageUtil.getMessage(messageKey);
        return new BusinessResp<>(code, message);
    }


}
