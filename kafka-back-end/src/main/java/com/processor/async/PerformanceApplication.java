package com.processor.async;

import com.processor.async.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PerformanceApplication implements CommandLineRunner {

	@Autowired
	private KafkaProducerService producerService;

	public static void main(String[] args) {
		SpringApplication.run(PerformanceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		long inicio = System.currentTimeMillis();
		producerService.enviar5000Mensagens();
		long fim = System.currentTimeMillis();
		System.out.println("Tempo para enviar mensagens: " + (fim - inicio) + "ms");
	}
}
