package com.itmo.tinkoffinvestementbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfiguration {

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(3);
    }
}
