package com.itmo.tinkoffinvestementbot.service;



import tinkoffinvestementbot.dto.CandlesDto;

import java.util.List;

public interface CandleService {
    List<CandlesDto> getCandlesByTicker(String ticker);

}
