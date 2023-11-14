package com.itmo.tinkoffinvestementbot.service.client;

import tinkoffinvestementbot.dto.OrderDto;

public interface OrderServiceClient {
    void registerOrder(OrderDto orderDto);
}
