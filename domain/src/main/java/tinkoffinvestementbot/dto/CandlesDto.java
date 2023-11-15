package tinkoffinvestementbot.dto;

import tinkoffinvestementbot.model.Candle;
import tinkoffinvestementbot.model.InstrumentInfo;

import java.util.List;

public record CandlesDto(InstrumentInfo instrumentInfo, List<Candle> candles) {
}
