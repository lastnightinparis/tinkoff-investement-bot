package com.itmo.tinkoffinvestementbot.configuration;

import com.itmo.tinkoffinvestementbot.config.RestConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableCaching
public class OrchestratorConfiguration {
    @Bean
    public RestConfig apiConfig() {
        return new RestConfig();
    }

}
