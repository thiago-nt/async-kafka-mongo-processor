package com.processor.async.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public List<CompletableFuture<SendResult<String, String>>> enviar5000Mensagens() {
        List<CompletableFuture<SendResult<String, String>>> futures = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("meu-topico", "Mensagem " + i);
            futures.add(future);
        }
        return futures;
    }
}
