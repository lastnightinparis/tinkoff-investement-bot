package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.dto.CandlesDto;

import java.util.List;

public interface CandleService {
    List<CandlesDto> getCandlesByTicker(String ticker);

}
