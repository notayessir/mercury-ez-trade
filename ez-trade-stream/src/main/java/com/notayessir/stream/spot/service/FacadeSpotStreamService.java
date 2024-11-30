package com.notayessir.stream.spot.service;


import com.alibaba.fastjson2.JSONObject;
import com.notayessir.quote.api.spot.mq.AggTradeRecordDTO;
import com.notayessir.quote.api.spot.mq.DepthDTO;
import com.notayessir.quote.api.spot.mq.KLineDTO;
import com.notayessir.stream.spot.bo.WrappedSessionMessageBO;
import com.notayessir.stream.spot.bo.WrappedWebSocketSessionBO;
import com.notayessir.stream.spot.constant.EnumMethodCode;
import com.notayessir.stream.spot.vo.MessageReqVO;
import com.notayessir.stream.spot.vo.MessageRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.util.Objects;

@Service
public class FacadeSpotStreamService {


    @Autowired
    private WebSocketSessionService webSocketSessionService;



    public void handleAggTradeMessage(AggTradeRecordDTO aggTradeRecordDTO) {
        String topic = aggTradeRecordDTO.getCode();
        TextMessage message = new TextMessage(JSONObject.toJSONString(aggTradeRecordDTO));
        WrappedSessionMessageBO messageBO = new WrappedSessionMessageBO(topic, message);
        webSocketSessionService.sendMessage(messageBO);
    }

    public void handleDepthMessage(DepthDTO depthDTO) {
        String topic = depthDTO.getCode();
        TextMessage message = new TextMessage(JSONObject.toJSONString(depthDTO));
        WrappedSessionMessageBO messageBO = new WrappedSessionMessageBO(topic, message);
        webSocketSessionService.sendMessage(messageBO);
    }

    public void handleKLineMessage(KLineDTO kLineDTO) {
        String topic = kLineDTO.getCode();
        TextMessage message = new TextMessage(JSONObject.toJSONString(kLineDTO));
        WrappedSessionMessageBO messageBO = new WrappedSessionMessageBO(topic, message);
        webSocketSessionService.sendMessage(messageBO);
    }

    public MessageRespVO handleMessageReq(MessageReqVO messageReq) {
        EnumMethodCode methodCode = EnumMethodCode.getByMethodCode(messageReq.getMethod());
        if (Objects.isNull(methodCode)){
            return new MessageRespVO(messageReq.getId());
        }
        String sessionId = messageReq.getSession().getId();
        if (methodCode == EnumMethodCode.SUBSCRIBE){
            webSocketSessionService.subscribe(sessionId, messageReq.getParams());
        } else if (methodCode == EnumMethodCode.UNSUBSCRIBE) {
            webSocketSessionService.unsubscribe(sessionId, messageReq.getParams());
        }
        return new MessageRespVO(messageReq.getId());
    }

    public void addSession(String sessionId, WrappedWebSocketSessionBO socketSessionBO) {
        webSocketSessionService.addSession(sessionId, socketSessionBO);
    }

    public void removeSession(String sessionId) {
        webSocketSessionService.removeSession(sessionId);
    }
}
