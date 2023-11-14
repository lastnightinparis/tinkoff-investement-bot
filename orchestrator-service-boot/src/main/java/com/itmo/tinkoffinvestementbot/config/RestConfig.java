package com.itmo.tinkoffinvestementbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rest")
public class RestConfig {
    private String stockServiceUrl;
    private String strategyServiceUrl;
    private String orderServiceUrl;
}
