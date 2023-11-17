package com.itmo.tinkoffinvestementbot.service;

import tinkoffinvestementbot.dto.strategies.RequestStrategyDto;
import tinkoffinvestementbot.dto.strategies.ResponseStrategyDto;
import tinkoffinvestementbot.dto.strategies.RunStrategyRequestDto;

import java.util.List;

public interface StrategyService {
    ResponseStrategyDto getInfo(RequestStrategyDto requestStrategyDto);

    void run(RunStrategyRequestDto runStrategyRequestDto);

    List<ResponseStrategyDto> list();
}
