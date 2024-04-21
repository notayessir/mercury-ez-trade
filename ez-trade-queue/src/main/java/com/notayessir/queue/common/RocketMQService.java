//package com.notayessir.queue.common;
//
//
//import com.alibaba.fastjson2.JSONObject;
//import com.notayessir.common.mq.BaseEvent;
//import com.notayessir.common.mq.MQResult;
//import org.apache.rocketmq.client.producer.*;
//import org.apache.rocketmq.common.message.MessageConst;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RocketMQService {
//
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//
////    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//
//
//
//    public MQResult sendTransactionMessage(String topic, String tag, BaseEvent event){
//        Message<?> message = MessageBuilder.withPayload(event)
//                .setHeader(MessageConst.PROPERTY_KEYS, event.getId())
//                .setHeader(MessageConst.PROPERTY_TAGS, tag)
//                .build();
//
//
//        TransactionSendResult sendResult = rocketMQTemplate
//                .sendMessageInTransaction(topic, message, event.getId());
//        boolean success = sendResult.getLocalTransactionState().equals(LocalTransactionState.COMMIT_MESSAGE)
//                && sendResult.getSendStatus().equals(SendStatus.SEND_OK);
//
//        return MQResult.builder()
//                .success(success)
//                .messageId(sendResult.getMsgId())
//                .build();
//    }
//
//    public MQResult sendNormalMessage(String topic, String tag, BaseEvent event){
//        Message<?> message = MessageBuilder.withPayload(event)
//                .setHeader(MessageConst.PROPERTY_KEYS, event.getId())
//                .setHeader(MessageConst.PROPERTY_TAGS, tag)
//                .build();
//
//
//        SendResult sendResult = rocketMQTemplate.syncSend(topic, message);
//        return MQResult.builder()
//                    .success(true)
//                    .messageId(sendResult.getMsgId())
//                    .build();
//    }
//
//
//    public void sendNormalMessageAsync(String topic, String tag, BaseEvent event){
//        Message<?> message = MessageBuilder.withPayload(event)
//                .setHeader(MessageConst.PROPERTY_KEYS, event.getId())
//                .setHeader(MessageConst.PROPERTY_TAGS, tag)
//                .build();
//        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
//            @Override
//            public void onSuccess(SendResult sendResult) {
//                logger.info("Send message successfully, messageId={}", sendResult.getMsgId());
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//                logger.error("Failed to send message, event: {} ", JSONObject.toJSONString(event), throwable);
//
//            }
//        });
//    }
//
//
//
//}
