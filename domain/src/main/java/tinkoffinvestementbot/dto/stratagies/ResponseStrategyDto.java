package tinkoffinvestementbot.dto.stratagies;

import java.math.BigDecimal;
import java.util.List;

public record ResponseStrategyDto(
        List<TradeSignal> enterPoints,
        List<TradeSignal> exitPoints) {
}
