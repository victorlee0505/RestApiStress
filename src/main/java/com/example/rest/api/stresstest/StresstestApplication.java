package com.example.rest.api.stresstest;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class StresstestApplication {

	public static void main(String[] args) {
		SpringApplication.run(StresstestApplication.class, args);
	}

	@Bean(name="apiExecutor")
    public Executor apiExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        // executor.setMaxPoolSize(250);
		// executor.setQueueCapacity(10);
        executor.initialize();
        return executor;
    }

}
