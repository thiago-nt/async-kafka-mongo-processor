package com.processor.async.consumer;

import com.processor.async.domain.DocumentMongo;
import com.processor.async.repository.DocumentMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class KafkaConsumerService {

    @Autowired
    private DocumentMongoRepository documentMongoRepository;

    @KafkaListener(topics = "meu-topico", groupId = "grupo-teste")
    public void consumirMensagem(String mensagem) {
        DocumentMongo doc = new DocumentMongo(mensagem, Instant.now());
        documentMongoRepository.save(doc);
    }
}
