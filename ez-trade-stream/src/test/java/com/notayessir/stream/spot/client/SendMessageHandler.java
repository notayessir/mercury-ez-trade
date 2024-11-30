package com.notayessir.stream.spot.client;

import com.notayessir.stream.spot.constant.StreamTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

@Slf4j
public class SendMessageHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("afterConnected");
        int i = 0;
        while (i < 2000){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i++;
            String message = "hah" + i;
            session.send(StreamTopic.SPOT_STREAM_PREFIX + "/coin/1", message);
        }
    }




}
