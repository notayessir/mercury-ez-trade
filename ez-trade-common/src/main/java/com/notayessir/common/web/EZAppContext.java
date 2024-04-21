package com.notayessir.common.web;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class EZAppContext implements ApplicationContextAware {

    private static ApplicationContext appContext;



    @PostConstruct
    public void init(){
        log.info(this.getClass().getName() + " loaded.");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    public static <C> C getBean(Class<?> clz){
        return (C) appContext.getBean(clz);
    }

    public static Object getBean(String name){
        return appContext.getBean(name);
    }
}
