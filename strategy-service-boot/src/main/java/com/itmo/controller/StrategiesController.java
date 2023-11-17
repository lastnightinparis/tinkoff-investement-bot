package com.itmo.controller;

import com.itmo.service.TradeAlgorithmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tinkoffinvestementbot.dto.stratagies.RequestStrategyDto;
import tinkoffinvestementbot.dto.stratagies.ResponseStrategyDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(StrategiesController.ROOT)
public class StrategiesController {
    public static final String ROOT = "/strategies";
    private final TradeAlgorithmsService tradeAlgorithmsService;

    @PostMapping
    public ResponseStrategyDto getTradePoints(@RequestBody RequestStrategyDto dto) {
        return tradeAlgorithmsService.getTradePoints(dto);
    }

    @GetMapping
    public ResponseStrategyDto getStrategies(@RequestBody RequestStrategyDto dto) {
        return tradeAlgorithmsService.getTradePoints(dto);
    }
}
