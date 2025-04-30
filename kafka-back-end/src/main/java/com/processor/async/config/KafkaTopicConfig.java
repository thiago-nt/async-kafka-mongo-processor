package com.processor.async.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic meuTopico() {
        return new NewTopic("meu-topico", 10, (short) 1);
    }
}

