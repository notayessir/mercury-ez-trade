package com.notayessir.stream.spot.bo;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketMessage;

@Getter
@Setter
public class WrappedSessionMessageBO {


    private String topic;
    private WebSocketMessage<?> message;

    public WrappedSessionMessageBO(String topic, WebSocketMessage<?> message) {
        this.topic = topic;
        this.message = message;
    }
}
