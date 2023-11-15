package com.itmo.tinkoffinvestementbot.service;

import tinkoffinvestementbot.dto.stratagies.RequestStrategyDto;
import tinkoffinvestementbot.dto.stratagies.ResponseStrategyDto;
import tinkoffinvestementbot.dto.stratagies.RunStrategyRequestDto;

import java.util.List;

public interface StrategyService {
    ResponseStrategyDto getInfo(RequestStrategyDto requestStrategyDto);

    void run(RunStrategyRequestDto runStrategyRequestDto);

    List<ResponseStrategyDto> list();
}
