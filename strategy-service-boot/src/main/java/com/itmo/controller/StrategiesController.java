package com.itmo.controller;

import com.itmo.service.TradeAlgorithmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tinkoffinvestementbot.dto.strategies.RequestStrategyDto;
import tinkoffinvestementbot.dto.strategies.ResponseStrategyDto;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = StrategiesController.ROOT, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
public class StrategiesController {
    public static final String ROOT = "/strategies";
    private final TradeAlgorithmsService tradeAlgorithmsService;

    @PostMapping
    public ResponseStrategyDto getTradePoints(@RequestBody RequestStrategyDto dto) {
        return tradeAlgorithmsService.getTradePoints(dto);
    }

}
