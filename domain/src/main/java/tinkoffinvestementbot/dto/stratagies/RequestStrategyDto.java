package tinkoffinvestementbot.dto.stratagies;

import tinkoffinvestementbot.model.strategies.StratagyType;

import java.util.Map;

public record RequestStrategyDto(
        StratagyType stratagyName,
        StrategyDto strategy
) { }
