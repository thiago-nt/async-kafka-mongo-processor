package com.processor.async.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void enviar5000Mensagens() {
        for (int i = 0; i < 5000; i++) {
            kafkaTemplate.send("meu-topico", "Mensagem nº " + i);
        }
    }
}
