package com.notayessir.stream.spot.service;

import cn.hutool.core.collection.CollectionUtil;
import com.notayessir.stream.spot.bo.SendTaskBO;
import com.notayessir.stream.spot.bo.WrappedSessionMessageBO;
import com.notayessir.stream.spot.bo.WrappedWebSocketSessionBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WebSocketSessionService {

    @Autowired
    @Qualifier("messagePushExecutor")
    private ThreadPoolExecutor messagePushExecutor;

    private final Map<String, WrappedWebSocketSessionBO> allSessionMap = new ConcurrentHashMap<>();
    private final Queue<WrappedSessionMessageBO> messageQueue = new ConcurrentLinkedQueue<>();

    private void subscribe(String sessionId, String topic){
        if (StringUtils.isBlank(topic) || !allSessionMap.containsKey(sessionId)){
            return;
        }
        WrappedWebSocketSessionBO wrappedWebSocketSessionBO = allSessionMap.get(sessionId);
        wrappedWebSocketSessionBO.addTopic(topic);
    }

    public void subscribe(String sessionId, List<String> topics){
        if (CollectionUtil.isEmpty((topics)) || !allSessionMap.containsKey(sessionId)){
            return;
        }
        WrappedWebSocketSessionBO wrappedWebSocketSessionBO = allSessionMap.get(sessionId);
        wrappedWebSocketSessionBO.addTopics(topics);
    }


    private void unsubscribe(String sessionId, String topic){
        if (StringUtils.isBlank(topic) || !allSessionMap.containsKey(sessionId)){
            return;
        }
        WrappedWebSocketSessionBO wrappedWebSocketSessionBO = allSessionMap.get(sessionId);
        wrappedWebSocketSessionBO.removeTopic(topic);
    }

    public void unsubscribe(String sessionId, List<String> topics){
        if (CollectionUtil.isEmpty(topics) || !allSessionMap.containsKey(sessionId)){
            return;
        }
        WrappedWebSocketSessionBO wrappedWebSocketSessionBO = allSessionMap.get(sessionId);
        wrappedWebSocketSessionBO.removeTopics(topics);
    }

    public void sendMessages(List<WrappedSessionMessageBO> list){
        if (CollectionUtil.isEmpty(list)){
            return;
        }
        messageQueue.addAll(list);
        trySendMessage();
    }


    public void sendMessage(WrappedSessionMessageBO messageBO){
        if (Objects.isNull(messageBO)){
            return;
        }
        messageQueue.add(messageBO);
        trySendMessage();
    }

    private void trySendMessage() {
        while (!messageQueue.isEmpty()){
            WrappedSessionMessageBO wrappedMessage = messageQueue.poll();
            List<WrappedWebSocketSessionBO> wrappedSessions = getSubscribers(wrappedMessage.getTopic());
            if (CollectionUtil.isEmpty(wrappedSessions)){
                continue;
            }
            for (WrappedWebSocketSessionBO wrappedSession : wrappedSessions) {
                SendTaskBO sendTaskBO = new SendTaskBO(wrappedMessage, wrappedSession);
                messagePushExecutor.execute(sendTaskBO);
            }
        }
    }

    private List<WrappedWebSocketSessionBO> getSubscribers(String topic) {
        Collection<WrappedWebSocketSessionBO> values = allSessionMap.values();
        if (CollectionUtil.isEmpty(values)){
            return Collections.emptyList();
        }
        return values.stream().filter(item -> item.containsTopic(topic)).collect(Collectors.toList());
    }

    public void removeSession(String sessionId){
        allSessionMap.remove(sessionId);
    }

    public void addSession(String sessionId, WrappedWebSocketSessionBO session){
        allSessionMap.putIfAbsent(sessionId, session);
    }


}
