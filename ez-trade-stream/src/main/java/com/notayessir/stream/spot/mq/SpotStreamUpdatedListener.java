package com.notayessir.stream.spot.mq;


import com.alibaba.fastjson2.JSONObject;
import com.notayessir.quote.api.spot.mq.QuoteTopic;
import com.notayessir.quote.api.spot.mq.QuoteUpdatedEvent;
import com.notayessir.stream.spot.service.FacadeSpotStreamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class SpotStreamUpdatedListener {

    private final String GROUP_ID = "StreamHandleGroup";


    @Autowired
    private FacadeSpotStreamService facadeStreamService;



    @KafkaListener(groupId = GROUP_ID, topics = QuoteTopic.QUOTE_UPDATED_TOPIC, concurrency = "2")
    public void handleStreamUpdatedEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String key = record.key();
        long offset = record.offset();
        String value = record.value();

        log.info("receive event: {}", value);

        QuoteUpdatedEvent event = JSONObject.parseObject(value, QuoteUpdatedEvent.class);
        try {

            facadeStreamService.handleStreamUpdatedEvent(event);

            ack.acknowledge();

        } catch (Exception e){
            log.warn("fail to handle stream event:{}", event.getRequestId(), e);
            ack.nack(Duration.ofMillis(22));
        }
    }



}
