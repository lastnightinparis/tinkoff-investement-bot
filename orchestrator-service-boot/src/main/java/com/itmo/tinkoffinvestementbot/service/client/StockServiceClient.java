package com.itmo.tinkoffinvestementbot.service.client;

import tinkoffinvestementbot.dto.CandlesDto;

import java.util.List;

public interface StockServiceClient {
    List<CandlesDto> getCandles(String ticker);
}
