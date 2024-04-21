package com.notayessir.stream.config;


import com.notayessir.stream.spot.constant.StreamTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@EnableWebSocketMessageBroker
@Configuration
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Lazy
    @Autowired
    private TaskScheduler messageBrokerTaskScheduler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("stream-service/public-api/spot/v1");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker(StreamTopic.SPOT_STREAM_PREFIX)
                .setHeartbeatValue(new long[] {10000, 20000})
                .setTaskScheduler(messageBrokerTaskScheduler);
    }


}
