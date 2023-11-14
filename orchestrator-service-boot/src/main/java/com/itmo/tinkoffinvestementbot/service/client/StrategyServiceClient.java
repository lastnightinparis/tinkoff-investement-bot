package com.itmo.tinkoffinvestementbot.service.client;

import tinkoffinvestementbot.dto.EventDto;
import tinkoffinvestementbot.dto.OrderDto;

import javax.annotation.Nullable;

public interface StrategyServiceClient {
    @Nullable OrderDto fireEvent(EventDto event);
}
