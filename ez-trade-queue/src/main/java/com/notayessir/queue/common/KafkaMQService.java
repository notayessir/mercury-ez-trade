package com.notayessir.queue.common;


import com.alibaba.fastjson2.JSONObject;
import com.notayessir.user.api.order.mq.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
@Service
public class KafkaMQService {




    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public boolean sendMessage(String topic, OrderEvent event){
        ProducerRecord<String, String> record
                = new ProducerRecord<>(topic, event.getCoinId().toString(), JSONObject.toJSONString(event));
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
        AtomicBoolean success = new AtomicBoolean(true);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                success.set(false);
            }
        });
        return success.get();
    }
}
