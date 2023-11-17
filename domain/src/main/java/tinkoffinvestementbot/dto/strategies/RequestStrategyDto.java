package tinkoffinvestementbot.dto.strategies;

import tinkoffinvestementbot.model.strategies.StrategyType;

public record RequestStrategyDto(
        StrategyType strategyName,
        StrategyDto strategy
) { }
