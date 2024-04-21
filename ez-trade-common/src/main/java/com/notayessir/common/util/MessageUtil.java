package com.notayessir.common.util;


import com.notayessir.common.web.EZAppContext;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class MessageUtil {

    public static String getMessage(String key, Object[] args){
        Locale locale = LocaleContextHolder.getLocale();
        MessageSource messageSource = EZAppContext.getBean(MessageSource.class);
        try {
           return messageSource.getMessage(key, args, locale);
        } catch (NoSuchMessageException e){
           return key;
        }
    }


    public static String getMessage(String key){
        Locale locale = LocaleContextHolder.getLocale();
        MessageSource messageSource = EZAppContext.getBean(MessageSource.class);
        try {
            return messageSource.getMessage(key, null, locale);
        } catch (NoSuchMessageException e){
            return key;
        }
    }

}
