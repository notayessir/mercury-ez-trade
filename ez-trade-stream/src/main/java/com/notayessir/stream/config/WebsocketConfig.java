package com.notayessir.stream.config;


import com.notayessir.stream.spot.service.SpotWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    private SpotWebSocketHandler spotWebsocketHandler;

    @Autowired
    private SimpMessagingTemplate template;



    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(spotWebsocketHandler, "/stream-service/spot/public-api/v1/quote");


    }
}
