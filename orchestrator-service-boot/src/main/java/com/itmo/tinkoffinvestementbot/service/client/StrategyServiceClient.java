package com.itmo.tinkoffinvestementbot.service.client;

import tinkoffinvestementbot.dto.EventDto;
import tinkoffinvestementbot.dto.OrderDto;
import tinkoffinvestementbot.dto.strategies.RequestStrategyDto;
import tinkoffinvestementbot.dto.strategies.ResponseStrategyDto;
import tinkoffinvestementbot.dto.strategies.RunStrategyRequestDto;

import javax.annotation.Nullable;
import java.util.List;

public interface StrategyServiceClient {
    @Nullable
    OrderDto fireEvent(EventDto event);

    ResponseStrategyDto getInfo(RequestStrategyDto requestStrategyDto);

    void run(RunStrategyRequestDto runStrategyRequestDto);
    List<ResponseStrategyDto> list();
}
