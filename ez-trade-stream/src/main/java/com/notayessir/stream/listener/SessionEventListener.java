package com.notayessir.stream.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;


@Slf4j
@Component
public class SessionEventListener {


    @EventListener
    public void onBrokerAvailabilityEvent(BrokerAvailabilityEvent event) {
        log.info("BrokerAvailabilityEvent: {}", event);
    }

    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent event) {
        log.info("SessionConnectedEvent: {}", event);
    }

    @EventListener
    public void onSessionConnectEvent(SessionConnectEvent event) {
        log.info("SessionConnectEvent: {}", event);
    }

    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        log.info("SessionDisconnectEvent: {}", event);
    }


    @EventListener
    public void onSessionSubscribeEvent(SessionSubscribeEvent event) {
        log.info("SessionSubscribeEvent: {}", event);
    }

    @EventListener
    public void onSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        log.info("SessionUnsubscribeEvent: {}", event);
    }
}
