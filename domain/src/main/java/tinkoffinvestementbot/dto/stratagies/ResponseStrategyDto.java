package tinkoffinvestementbot.dto.stratagies;

import java.math.BigDecimal;

public record ResponseStrategyDto(BigDecimal buyPrice, BigDecimal sellPrice) {
}
