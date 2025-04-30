package com.processor.async.consumer;

import com.processor.async.domain.DocumentMongo;
import com.processor.async.repository.DocumentMongoRepository;
import jakarta.annotation.PreDestroy;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private DocumentMongoRepository documentMongoRepository;
    @Setter
    private CountDownLatch latch;
    private final Set<String> threadsAtivas = ConcurrentHashMap.newKeySet();
    private static final ConcurrentHashMap<String, List<Long>> temposPorThread = new ConcurrentHashMap<>();

    @KafkaListener(
            topics = "meu-topico",
            groupId = "grupo-teste",
            concurrency = "10"
    )
    public void consumirMensagem(String mensagem) {
        long inicio = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();
        threadsAtivas.add(threadName);

        DocumentMongo doc = new DocumentMongo(mensagem, Instant.now());
        documentMongoRepository.save(doc);

        long duracao = System.currentTimeMillis() - inicio;

        temposPorThread.computeIfAbsent(threadName, k -> new ArrayList<>()).add(duracao);
        latch.countDown();
    }

    @PreDestroy
    public void exibirRelatorioFinal() {
        log.info("\n📊 Relatório de tempo médio por thread:");
        for (Map.Entry<String, List<Long>> entry : temposPorThread.entrySet()) {
            String thread = entry.getKey();
            List<Long> tempos = entry.getValue();
            double media = tempos.stream().mapToLong(Long::longValue).average().orElse(0.0);
            log.info("➡️  {}: Média = {} ms, Total mensagens = {}", thread, String.format("%.2f", media), tempos.size());
        }

        long totalMensagens = temposPorThread.values().stream().mapToLong(List::size).sum();
        long totalTempo = temposPorThread.values().stream()
                .flatMap(Collection::stream)
                .mapToLong(Long::longValue).sum();
        double mediaGeral = (totalMensagens > 0) ? (double) totalTempo / totalMensagens : 0;
        log.info(String.format("📈 Tempo médio geral por mensagem: %.2f ms", mediaGeral));
    }
}