package com.itmo.tinkoffinvestementbot.service;

import tinkoffinvestementbot.model.strategies.StrategyType;

import java.util.UUID;

public interface SubscribeService {
    UUID subscribe(StrategyType strategyType, Long userId, String ticker, Double riskRating);

    void cancel(UUID taskId);
}
