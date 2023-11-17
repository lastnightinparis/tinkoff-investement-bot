package tinkoffinvestementbot.dto.strategies;

import tinkoffinvestementbot.model.strategies.StratagyType;

public record RequestStrategyDto(
        StratagyType strategyName,
        StrategyDto strategy
) { }
