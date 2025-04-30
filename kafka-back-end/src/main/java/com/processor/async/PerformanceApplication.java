package com.processor.async;

import com.processor.async.consumer.KafkaConsumerService;
import com.processor.async.service.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class PerformanceApplication implements CommandLineRunner {

	@Autowired
	private KafkaProducerService producerService;

	@Autowired
	private KafkaConsumerService consumerService;

	private static final Logger log = LoggerFactory.getLogger(PerformanceApplication.class);
	@Autowired
	private ApplicationContext context;
	@Autowired
	private KafkaListenerEndpointRegistry registry;

	public static void main(String[] args) {
		SpringApplication.run(PerformanceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		long inicio = System.currentTimeMillis();
		log.info("Início da operação: " + inicio);
		CountDownLatch latch = new CountDownLatch(5000);
		consumerService.setLatch(latch);

		List<CompletableFuture<SendResult<String, String>>> futures = producerService.enviar5000Mensagens();
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		latch.await();
		long fim = System.currentTimeMillis();
		long duracaoMs = fim - inicio;
		long segundos = duracaoMs / 1000;
		long minutos = segundos / 60;

		log.info("Fim da operação. Tempo total: " + duracaoMs + "ms (" + segundos + "s, " + minutos + "min)");
		System.out.println("Tempo para enviar mensagens: " + duracaoMs + "ms (" + segundos + "s, " + minutos + "min)");

		registry.getListenerContainers().forEach(Lifecycle::stop);

		if (context instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) context).close();
		}
	}
}
