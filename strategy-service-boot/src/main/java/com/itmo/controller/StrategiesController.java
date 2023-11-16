package com.itmo.controller;

import com.itmo.dto.RequestStrategyDto;
import com.itmo.dto.ResponseStrategyDto;
import com.itmo.service.TradeAlgorithmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
