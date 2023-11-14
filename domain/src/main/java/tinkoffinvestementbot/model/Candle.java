package tinkoffinvestementbot.model;

import java.math.BigDecimal;


public record Candle(BigDecimal openPrice, BigDecimal closePrice, BigDecimal minPer1Batch, BigDecimal maxPer1Batch,
                     long volume, String time) {
}
