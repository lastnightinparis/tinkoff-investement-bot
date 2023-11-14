package com.itmo.tinkoffinvestementbot.dto;

import com.itmo.tinkoffinvestementbot.model.Candle;
import com.itmo.tinkoffinvestementbot.model.StockInfo;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@AllArgsConstructor
@Value
public class CandlesDto {
    StockInfo stockInfo;
    List<Candle> candles;
}
