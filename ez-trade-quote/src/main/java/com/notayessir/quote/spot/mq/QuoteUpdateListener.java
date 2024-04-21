package com.notayessir.quote.spot.mq;


import com.alibaba.fastjson2.JSONObject;
import com.notayessir.bo.MatchResultBO;
import com.notayessir.quote.spot.service.FacadeSpotQuoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class QuoteUpdateListener {

    private final String GROUP_ID = "QuoteHandleGroup";

    private final String MATCH_RESULT_TOPIC = "match_result_topic";

    @Autowired
    private FacadeSpotQuoteService facadeSpotQuoteService;

    @KafkaListener(groupId = GROUP_ID, topics = MATCH_RESULT_TOPIC, concurrency = "2")
    public void handleQuoteUpdateEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String key = record.key();
        long offset = record.offset();
        String value = record.value();

        log.info("receive event: {}", value);

        MatchResultBO event = JSONObject.parseObject(value, MatchResultBO.class);
        try {

            facadeSpotQuoteService.handleQuoteUpdateEvent(event);
            ack.acknowledge();

        } catch (Exception e){
            log.warn("error orderId:{}", event.getTakerOrder().getOrderId(), e);
            ack.nack(Duration.ofMillis(22));
        }
    }



}
