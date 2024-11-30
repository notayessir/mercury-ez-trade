package com.notayessir.stream.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {


    @Bean(name = "messagePushExecutor")
    public ThreadPoolExecutor messagePushExecutor() {
        return new ThreadPoolExecutor(
                50,
                70,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100_000),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
    }


}
