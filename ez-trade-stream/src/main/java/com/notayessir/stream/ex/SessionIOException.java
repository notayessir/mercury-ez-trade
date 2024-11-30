package com.notayessir.stream.ex;


import com.notayessir.stream.spot.bo.WrappedSessionMessageBO;
import org.springframework.web.socket.WebSocketSession;

public class SessionIOException extends Exception{

    private WebSocketSession session;
    private WrappedSessionMessageBO messageBO;



}
