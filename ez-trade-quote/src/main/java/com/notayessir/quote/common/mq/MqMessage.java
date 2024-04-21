package com.notayessir.quote.common.mq;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MqMessage {

    private String topic;
    private Object data;

    public MqMessage() {
    }

    public MqMessage(String topic, Object data) {
        this.topic = topic;
        this.data = data;
    }
}
