package com.itmo.tinkoffinvestementbot.configuration;

import com.itmo.tinkoffinvestementbot.config.RestConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrchestratorConfiguration {
    @Bean
    public RestConfig apiConfig() {
        return new RestConfig();
    }
}
