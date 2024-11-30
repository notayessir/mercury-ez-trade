package com.notayessir.stream.spot.bo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.Objects;

@Getter
@Setter
public class SendTaskBO implements Runnable{


    private WrappedSessionMessageBO wrappedMessage;
    private WrappedWebSocketSessionBO wrappedSession;

    public SendTaskBO(WrappedSessionMessageBO wrappedMessage, WrappedWebSocketSessionBO wrappedSession) {
        this.wrappedMessage = wrappedMessage;
        this.wrappedSession = wrappedSession;
    }

    @Override
    public void run() {
        WebSocketMessage<?> message = wrappedMessage.getMessage();
        ConcurrentWebSocketSessionDecorator webSocketSession = wrappedSession.getWebSocketSession();
        try {
            webSocketSession.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
