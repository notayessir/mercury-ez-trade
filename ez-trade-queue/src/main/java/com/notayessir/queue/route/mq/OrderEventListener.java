package com.notayessir.queue.route.mq;


import com.alibaba.fastjson2.JSONObject;
import com.notayessir.queue.route.service.FacadeRouteService;
import com.notayessir.user.api.order.mq.OrderEvent;
import com.notayessir.user.api.order.mq.OrderTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
@Slf4j
public class OrderEventListener {


    private final String GROUP_ID = "doOrderRouteGroup";




    @Autowired
    private FacadeRouteService facadeRouteService;




    @KafkaListener(groupId = GROUP_ID, topics = OrderTopic.CREATE_ORDER_EVENT_TOPIC,
            concurrency = "3")
    public void handleOrderEvent(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String key = record.key();
        long offset = record.offset();
        String value = record.value();
        log.info("receive event: {}", value);

        OrderEvent event = JSONObject.parseObject(value, OrderEvent.class);
        try {

            facadeRouteService.handleOrderEvent(event);
            ack.acknowledge();

        } catch (Exception e){
            log.warn("error orderId:{}, command:{}", event.getOrderId(), event.getCommand(), e);
            ack.nack(Duration.ofMillis(22));

        }

    }


}
