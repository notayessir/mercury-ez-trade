package com.notayessir.common.web;


import com.notayessir.common.vo.BusinessResp;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class BusinessExceptionHandler {





    @PostConstruct
    public void init(){
        log.info(this.getClass().getName() + " loaded.");
    }

    /**
     * onEvent business exception
     * @param e BusinessException
     * @return  http response
     */

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BusinessResp<?> exceptionHandler(BusinessException e){

        return BusinessResp.error(e.getCode(), e.getMsg(), e.getArgs());
    }


    /**
     * onEvent unexpected exception
     * @param e Exception
     * @return  http response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BusinessResp<?> exceptionHandler(Exception e){
        log.error("capture unexpected Exception: ", e);
        return BusinessResp.error();
    }

}
