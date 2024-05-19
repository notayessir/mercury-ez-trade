package com.notayessir.queue.web.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.notayessir.common.web.interceptor.LocaleInterceptor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {




    /**
     * to solve the Exception:
     * 1. com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
     * No serializer found for class xxx (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
     * 2. Jackson with JSON: Unrecognized field, not marked as ignorable
     * @return  ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // long precision
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);


        // Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    /**
     * i18n support
     * @return  LocaleResolver
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // i18n onEvent
        registry.addInterceptor(new LocaleInterceptor()).addPathPatterns("/**");
    }


    /**
     * transaction util
     * @param transactionManager    transactionManager
     * @return                      TransactionTemplate
     */
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager){
        return new TransactionTemplate(transactionManager);
    }



}
