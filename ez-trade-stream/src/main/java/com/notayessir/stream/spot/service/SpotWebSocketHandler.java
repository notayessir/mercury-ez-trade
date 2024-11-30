package com.notayessir.stream.spot.service;

import com.alibaba.fastjson2.JSONObject;
import com.notayessir.stream.spot.bo.WrappedWebSocketSessionBO;
import com.notayessir.stream.spot.vo.MessageReqVO;
import com.notayessir.stream.spot.vo.MessageRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;


@Component
@Slf4j
public class SpotWebSocketHandler extends TextWebSocketHandler {


    @Autowired
    private FacadeSpotStreamService facadeSpotStreamService;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String string = new String(message.asBytes());
        MessageReqVO messageReq = JSONObject.parseObject(string, MessageReqVO.class);
        messageReq.setSession(session);
        MessageRespVO messageRespVO = facadeSpotStreamService.handleMessageReq(messageReq);
        try {
            session.sendMessage(new TextMessage(JSONObject.toJSONString(messageRespVO)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("connection established: {}", session.getId());
        facadeSpotStreamService.addSession(session.getId(), new WrappedWebSocketSessionBO(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("connection closed: {}", session.getId());
        facadeSpotStreamService.removeSession(session.getId());

    }
}
