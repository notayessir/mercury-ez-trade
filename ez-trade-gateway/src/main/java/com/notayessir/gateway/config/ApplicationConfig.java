package com.notayessir.gateway.config;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSONObject;
import com.notayessir.gateway.bo.TokenPayloadBO;
import com.notayessir.gateway.constant.BaseConstant;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

@Configuration
public class ApplicationConfig  {



    /**
     * i18n support
     * @return  LocaleResolver
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }



//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("modify_request_body_route", r -> r.path("/*/api/**")
//                        .filters(f -> f.modifyRequestBody(String.class, String.class,
//                                MediaType.APPLICATION_JSON_VALUE, (exchange, originalBody) -> {
//                                    // 获取请求头中的字段
//                                    String token = exchange.getRequest().getHeaders().getFirst(BaseConstant.AUTHORIZATION_HEADER);
//                                    // 将请求头中的字段放入请求体中
//
//                                    JSONObject body = JSONObject.parseObject(originalBody);
//                                    TokenPayloadBO payload = getPayload(token);
//                                    body.put(BaseConstant.USER_ID, payload.getSub());
//                                    return Mono.just(body.toJSONString());
//                                }))
//                        .uri("http://localhost:38090"))
//                .build();
//
//    }


    public TokenPayloadBO getPayload(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        String payload = jwt.getPayload().getClaimsJson().toString();
        return JSONObject.parseObject(payload, TokenPayloadBO.class);
    }


}
