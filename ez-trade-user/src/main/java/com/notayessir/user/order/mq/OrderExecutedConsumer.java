package com.notayessir.user.order.mq;


import com.alibaba.fastjson2.JSONObject;
import com.notayessir.engine.api.bo.MatchResultBO;
import com.notayessir.user.order.service.FacadeOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class OrderExecutedConsumer {

    private final String GROUP_ID = "OrderUpdateGroup";

    private final String MATCH_RESULT_TOPIC = "match_result_topic";


    @Autowired
    private FacadeOrderService facadeOrderService;



    @KafkaListener(groupId = GROUP_ID, topics = MATCH_RESULT_TOPIC, concurrency = "2")
    public void handleOrderExecutedEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String key = record.key();
        long offset = record.offset();
        String value = record.value();

        log.info("receive event: {}", value);

        MatchResultBO event = JSONObject.parseObject(value, MatchResultBO.class);
        try {

            facadeOrderService.handleOrderExecutedEvent(event);
            ack.acknowledge();

        } catch (Exception e){
            log.warn("error handle order, id:{}", event.getTakerOrder().getOrderId(), e);
            ack.nack(Duration.ofMillis(22));
        }
    }



}
