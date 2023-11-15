package com.itmo.tinkoffinvestementbot.service.trading;

import com.itmo.tinkoffinvestementbot.model.dto.trading.TempTradingOrderDto;

import java.util.Map;

public interface TempTradingOrderService {
    void create(
            Long userId,
            Long strategyId, Map<String, String> additionalParamNames
    );

    TempTradingOrderDto get(Long userId);
}
