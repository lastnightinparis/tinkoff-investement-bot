package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.service.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tinkoffinvestementbot.dto.stratagies.RequestStrategyDto;
import tinkoffinvestementbot.dto.stratagies.ResponseStrategyDto;
import tinkoffinvestementbot.dto.stratagies.RunStrategyRequestDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StrategyController {
    private final StrategyService strategyService;

    @GetMapping("/strategy/info")
    public ResponseStrategyDto getInfo(@RequestParam RequestStrategyDto requestStrategyDto) {
        return strategyService.getInfo(requestStrategyDto);
    }

    @PostMapping("/strategy/run")
    public void run(@RequestParam RunStrategyRequestDto runStrategyRequestDto) {
        strategyService.run(runStrategyRequestDto);
    }

    @GetMapping("/strategy/list")
    public List<ResponseStrategyDto> list() {
        return strategyService.list();
    }

}
