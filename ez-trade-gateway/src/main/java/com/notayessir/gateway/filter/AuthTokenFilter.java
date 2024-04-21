package com.notayessir.gateway.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.notayessir.common.vo.BusinessResp;
import com.notayessir.gateway.bo.TokenPayloadBO;
import com.notayessir.gateway.vo.EnumGatewayResponse;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class AuthTokenFilter implements GlobalFilter, Ordered {


    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String USER_ID = "userId";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = exchange.getRequest().getURI();
        String path = uri.getPath();
        String[] split = path.split("/");
        if (StringUtils.equals(split[2], "public-api")){
            return chain.filter(exchange);
        } else {
            // if contain api and admin-api, identification need to be verified
            String token = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
            boolean authentic = verifyToken(token);
            if (!authentic) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                BusinessResp<Void> resp = BusinessResp.normal(EnumGatewayResponse.TOKEN_EXPIRE.getCode(), EnumGatewayResponse.TOKEN_EXPIRE.getMessage());
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory().wrap(JSONObject.toJSONString(resp).getBytes())));
            }

            ServerHttpRequestDecorator requestDecorator = requestDecorator(exchange, token);
            return chain.filter(exchange.mutate().request(requestDecorator).build());
        }
    }

    private ServerHttpRequestDecorator requestDecorator(ServerWebExchange exchange, String token) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                Flux<DataBuffer> body = super.getBody();
                return body.map(dataBuffer -> {
                    byte[] content = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(content);
                    //释放掉内存
                    DataBufferUtils.release(dataBuffer);
                    //request body的json格式数据
                    String bodyJson = new String(content, StandardCharsets.UTF_8);
                    //转化成json对象
                    JSONObject jsonObject = JSON.parseObject(bodyJson);
                    TokenPayloadBO payload = getPayload(token);
                    // 对原先的body进行修改操作
                    jsonObject.put(USER_ID, payload.getSub());

                    String result = jsonObject.toJSONString();
                    //转成字节
                    byte[] bytes = result.getBytes();
                    NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
                    DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
                    buffer.write(bytes);
                    return buffer;
                });
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
                return httpHeaders;
            }

        };
    }



    @Override
    public int getOrder() {
        return 0;
    }


    public boolean verifyToken(String token){
        if (StringUtils.isBlank(token)){
            return false;
        }
        JWT jwt;
        try {
            jwt = JWTUtil.parseToken(token);
        }catch (Exception e){
            log.warn("invalid jwt token: {}", token);
            return false;
        }

        // checkAndInit if token is expired
        long expAt = Long.parseLong(jwt.getPayload("exp").toString());
        long currentTimeMillis = System.currentTimeMillis();
        long now = currentTimeMillis / 1000;
        return expAt >= now;
    }


    public TokenPayloadBO getPayload(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        String payload = jwt.getPayload().getClaimsJson().toString();
        return JSONObject.parseObject(payload, TokenPayloadBO.class);
    }



}
