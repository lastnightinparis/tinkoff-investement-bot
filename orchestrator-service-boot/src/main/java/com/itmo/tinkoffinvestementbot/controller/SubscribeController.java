package com.itmo.tinkoffinvestementbot.controller;

import com.itmo.tinkoffinvestementbot.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;
import tinkoffinvestementbot.model.strategies.StrategyType;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class SubscribeController {
    private final SubscribeService subscribeService;

    @GetMapping("/subscribe")
    public String subscribe(@RequestParam Long strategyId, @RequestParam Long userId, @RequestParam String ticker, @RequestParam Double riskRating) {
        val strategyType = strategyId == 1L
                ? StrategyType.MAC
                : StrategyType.MRP;
        return subscribeService.subscribe(strategyType, userId, ticker, riskRating).toString();
    }

    @DeleteMapping("/cancel/{taskId}")
    public void cancel(@PathVariable UUID taskId) {
        subscribeService.cancel(taskId);
    }
}
