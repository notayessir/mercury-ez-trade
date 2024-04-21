package com.notayessir.common.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;


public class LocaleInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String language = request.getHeader("Accept-Language");
        if (StringUtils.isBlank(language)){
            language = request.getParameter("language");
        } else {
            String[] split = language.split("[,;]");
            if (split.length > 1){
                language = split[0];
            }
        }

        if (StringUtils.isBlank(language)){
            language = "en_US";
        }
        Locale locale = org.springframework.util.StringUtils.parseLocale(language);
        LocaleContextHolder.setLocale(locale);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LocaleContextHolder.resetLocaleContext();
    }
}
