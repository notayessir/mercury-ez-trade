package com.notayessir.stream.spot.bo;

import cn.hutool.core.collection.CollectionUtil;
import com.notayessir.stream.spot.constant.StreamConstants;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class WrappedWebSocketSessionBO {


    @Getter
    private final ConcurrentWebSocketSessionDecorator webSocketSession;
    private final Set<String> topics = new CopyOnWriteArraySet<>();

    public WrappedWebSocketSessionBO(WebSocketSession webSocketSession) {
        this.webSocketSession = new ConcurrentWebSocketSessionDecorator(webSocketSession, StreamConstants.SEND_TIME_LIMIT, StreamConstants.BUFFER_SIZE_LIMIT);
    }

    public boolean containsTopic(String topic){
        return topics.contains(topic);
    }

    public void addTopics(List<String> list){
        if (CollectionUtil.isEmpty(list)){
            return;
        }
        topics.addAll(list);
    }

    public void addTopic(String topic){
        if (StringUtils.isBlank(topic)){
            return;
        }
        topics.add(topic);
    }


    public void removeTopic(String topic){
        if (StringUtils.isBlank(topic)){
            return;
        }
        topics.remove(topic);
    }

    public void removeTopics(List<String> list){
        if (CollectionUtil.isEmpty(list)){
            return;
        }
        topics.removeAll(list);
    }


}
