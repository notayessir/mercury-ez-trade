package com.notayessir.stream.spot.client;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SendMessageClient {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);

        stompClient.setMessageConverter(new StringMessageConverter());

        String url = "ws://127.0.0.1:38084/stream-service/spot/public-api/v1/quote";
        StompSessionHandler sessionHandler = new SendMessageHandler();
        CompletableFuture<StompSession> future = stompClient.connectAsync(url, sessionHandler);
        StompSession stompSession = future.get();
        System.out.println(stompSession);
        while (true);

    }

}
