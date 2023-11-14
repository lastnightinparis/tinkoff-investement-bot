package tinkoffinvestementbot.dto;

import tinkoffinvestementbot.model.EventType;

import java.util.List;

public record EventDto(EventType eventType, String ticker, List<CandlesDto> candles) {
}
