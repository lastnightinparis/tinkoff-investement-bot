package com.itmo.tinkoffinvestementbot.service.client;

import com.itmo.tinkoffinvestementbot.config.RestConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tinkoffinvestementbot.dto.EventDto;
import tinkoffinvestementbot.dto.OrderDto;

import javax.annotation.Nullable;

@Service
@RequiredArgsConstructor
public class StrategyServiceClientImpl implements StrategyServiceClient {
    private final RestConfig restConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public @Nullable OrderDto fireEvent(EventDto event) {
        String resourceUrl = String.join("/", restConfig.getStrategyServiceUrl(), "event", event.ticker());

        final ResponseEntity<OrderDto> exchange = restTemplate.exchange(resourceUrl,
                HttpMethod.GET,
                new HttpEntity<>(event),
                new ParameterizedTypeReference<OrderDto>() {
                }
        );

        final OrderDto body = exchange.getBody();
        return body;
    }
}
