package com.itmo.tinkoffinvestementbot.service.client;

import com.itmo.tinkoffinvestementbot.config.RestConfig;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tinkoffinvestementbot.dto.CandlesDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class StockServiceClientImpl implements StockServiceClient {
    private final RestConfig restConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public CandlesDto getCandles(String ticker) {
        val now = Instant.now();
        val resourceUrl = restConfig.getStockServiceUrl() + "/customCandles"
                + "?ticker=" + ticker
                + "&startDate=" + now.minus(1L, ChronoUnit.WEEKS)
                + "&endDate=" + now
                + "&intervalMin=60";

        val exchange = restTemplate.exchange(resourceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CandlesDto>() {
                });
        return exchange.getBody();

    }
}
