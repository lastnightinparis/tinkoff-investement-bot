package com.itmo.tinkoffinvestementbot.service.client;

import tinkoffinvestementbot.dto.CandlesDto;

public interface StockServiceClient {
    CandlesDto getCandles(String ticker);
}
