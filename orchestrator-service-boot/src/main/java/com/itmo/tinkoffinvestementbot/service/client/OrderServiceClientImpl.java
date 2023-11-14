package com.itmo.tinkoffinvestementbot.service.client;

import com.itmo.tinkoffinvestementbot.config.RestConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tinkoffinvestementbot.dto.OrderDto;

@Service
@RequiredArgsConstructor
public class OrderServiceClientImpl implements OrderServiceClient {
    private final RestConfig restConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void registerOrder(OrderDto orderDto) {
        String resourceUrl = String.join("/", restConfig.getStrategyServiceUrl(), "order");

        restTemplate.exchange(resourceUrl,
                HttpMethod.GET,
                new HttpEntity<>(orderDto),
                Void.class
        );
    }
}
