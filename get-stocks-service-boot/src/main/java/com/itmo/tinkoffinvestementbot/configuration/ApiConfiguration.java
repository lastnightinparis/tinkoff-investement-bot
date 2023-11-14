package com.itmo.tinkoffinvestementbot.configuration;

import com.itmo.tinkoffinvestementbot.config.ApiConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
public class ApiConfiguration {

    @Bean
    public ApiConfig apiConfig() {
        return new ApiConfig();
    }

    @Bean
    public InvestApi investApi(ApiConfig apiConfig) {
        return InvestApi.create(apiConfig.getToken());
    }
}
