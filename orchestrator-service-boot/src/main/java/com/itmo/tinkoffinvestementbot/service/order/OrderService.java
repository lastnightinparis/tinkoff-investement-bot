package com.itmo.tinkoffinvestementbot.service.order;

import tinkoffinvestementbot.dto.OrderDto;
import tinkoffinvestementbot.model.OrderResult;

public interface OrderService {
    OrderResult sendOrder(OrderDto orderDto);
}
