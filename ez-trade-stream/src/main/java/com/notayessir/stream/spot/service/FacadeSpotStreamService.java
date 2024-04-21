package com.notayessir.stream.spot.service;


import com.alibaba.fastjson2.JSONObject;
import com.notayessir.quote.api.spot.mq.QuoteUpdatedEvent;
import com.notayessir.stream.spot.constant.StreamTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FacadeSpotStreamService {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;



    public void handleStreamUpdatedEvent(QuoteUpdatedEvent event) {


        String topic = StreamTopic.SPOT_STREAM_PREFIX + "/coin/" + event.getQTickRecord().getCoinId();
        simpMessagingTemplate.convertAndSend(topic, JSONObject.toJSONString(event));
    }



}
