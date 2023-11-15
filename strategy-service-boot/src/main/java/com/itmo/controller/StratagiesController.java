package com.itmo.controller;

import com.itmo.service.TradeAlgorithmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tinkoffinvestementbot.dto.stratagies.RequestStrategyDto;
import tinkoffinvestementbot.dto.stratagies.ResponseStrategyDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(StratagiesController.ROOT)
public class StratagiesController {
    public static final String ROOT = "/stratagies";
    private final TradeAlgorithmsService tradeAlgorithmsService;

    @PostMapping
    public ResponseStrategyDto getTradePoints(@RequestBody RequestStrategyDto dto) {
        return tradeAlgorithmsService.getTradePoints(dto);
    }
}
