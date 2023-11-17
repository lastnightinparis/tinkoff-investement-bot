package com.itmo.tinkoffinvestementbot.service.client;

import com.itmo.tinkoffinvestementbot.config.RestConfig;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tinkoffinvestementbot.dto.EventDto;
import tinkoffinvestementbot.dto.OrderDto;
import tinkoffinvestementbot.dto.strategies.RequestStrategyDto;
import tinkoffinvestementbot.dto.strategies.ResponseStrategyDto;
import tinkoffinvestementbot.dto.strategies.RunStrategyRequestDto;

import javax.annotation.Nullable;
import java.util.List;

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

    @Override
    public ResponseStrategyDto getInfo(RequestStrategyDto requestStrategyDto) {
        val resourceUrl = String.join("/", restConfig.getStrategyServiceUrl(), "strategies");
        val headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        val entity = new HttpEntity<>(requestStrategyDto, headers);
        val exchange = restTemplate.exchange(resourceUrl, HttpMethod.POST, entity, ResponseStrategyDto.class);

        final ResponseStrategyDto body = exchange.getBody();
        return body;
    }

    @Override
    public void run(RunStrategyRequestDto runStrategyRequestDto) {
        String resourceUrl = String.join("/", restConfig.getStrategyServiceUrl(), "strategy/run");

        restTemplate.exchange(resourceUrl,
                HttpMethod.GET,
                new HttpEntity<>(runStrategyRequestDto),
                Void.class
        );
    }

    @Override
    public List<ResponseStrategyDto> list() {
        String resourceUrl = String.join("/", restConfig.getStrategyServiceUrl(), "strategy/list");

        final ResponseEntity<List<ResponseStrategyDto>> exchange = restTemplate.exchange(resourceUrl,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<ResponseStrategyDto>>() {
                }
        );

        final List<ResponseStrategyDto> body = exchange.getBody();
        return body;
    }
}
