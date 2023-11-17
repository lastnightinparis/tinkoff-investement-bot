package tinkoffinvestementbot.dto.strategies;

import java.util.List;

public record ResponseStrategyDto(
        List<TradeSignal> enterPoints,
        List<TradeSignal> exitPoints,
        List<String> additionalMessages
) { }
