package com.notayessir.quote.spot.mq;


import com.alibaba.fastjson2.JSONObject;
import com.notayessir.engine.api.bo.MatchResultBO;
import com.notayessir.quote.spot.service.FacadeSpotQuoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class AggTradeConsumer {

    private final String GROUP_ID = "AggTradeConsumer";

    private final String MATCH_RESULT_TOPIC = "match_result_topic";

    @Autowired
    private FacadeSpotQuoteService facadeSpotQuoteService;

    @KafkaListener(groupId = GROUP_ID, topics = MATCH_RESULT_TOPIC)
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String topic = record.topic();
        String key = record.key();
        long offset = record.offset();
        String value = record.value();

        log.info("receive message: {}", value);

        MatchResultBO event = JSONObject.parseObject(value, MatchResultBO.class);
        try {

            facadeSpotQuoteService.handleAggTrade(event);
            ack.acknowledge();

        } catch (DuplicateKeyException e){
            log.warn("duplicate key: {}", event.getTxSequence());
        } catch (Exception e){
            log.warn("invalid data: ",  e);
            ack.nack(Duration.ofMillis(22));
        }
    }



}
