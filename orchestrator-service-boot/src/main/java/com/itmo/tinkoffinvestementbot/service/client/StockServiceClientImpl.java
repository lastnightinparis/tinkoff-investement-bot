package com.itmo.tinkoffinvestementbot.service.client;

import com.itmo.tinkoffinvestementbot.config.RestConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tinkoffinvestementbot.dto.CandlesDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceClientImpl implements StockServiceClient {
    private final RestConfig restConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<CandlesDto> getCandles(String ticker) {
        String resourceUrl = String.join("/", restConfig.getStockServiceUrl(), "candles", ticker);

        final ResponseEntity<List<CandlesDto>> exchange = restTemplate.exchange(resourceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CandlesDto>>() {}
        );

        final List<CandlesDto> body = exchange.getBody();
        return body;
    }
}
