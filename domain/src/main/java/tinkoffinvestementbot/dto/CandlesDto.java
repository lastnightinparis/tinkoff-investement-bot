package tinkoffinvestementbot.dto;

import tinkoffinvestementbot.model.Candle;
import tinkoffinvestementbot.model.StockInfo;

import java.util.List;

public record CandlesDto(StockInfo stockInfo, List<Candle> candles) {
}
