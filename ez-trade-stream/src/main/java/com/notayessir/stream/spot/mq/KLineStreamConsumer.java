package com.notayessir.stream.spot.mq;


import com.alibaba.fastjson2.JSONObject;
import com.notayessir.quote.api.spot.mq.AggTradeRecordDTO;
import com.notayessir.quote.api.spot.mq.KLineDTO;
import com.notayessir.quote.api.spot.mq.QuoteTopic;
import com.notayessir.stream.spot.service.FacadeSpotStreamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KLineStreamConsumer {

    private final String GROUP_ID = "KLineStreamConsumer";


    @Autowired
    private FacadeSpotStreamService facadeStreamService;


    @KafkaListener(groupId = GROUP_ID, topics = QuoteTopic.K_LINE_TOPIC)
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String key = record.key();
        long offset = record.offset();
        String value = record.value();

        log.info("receive event: {}", value);

        try {
            KLineDTO kLineDTO = JSONObject.parseObject(value, KLineDTO.class);
            facadeStreamService.handleKLineMessage(kLineDTO);

        } catch (Exception e){

            log.warn("fail to handle stream event:{}", record.value(), e);

        } finally {
            ack.acknowledge();
        }
    }



}
