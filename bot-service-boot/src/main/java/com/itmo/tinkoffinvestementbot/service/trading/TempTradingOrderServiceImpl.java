package com.itmo.tinkoffinvestementbot.service.trading;

import com.itmo.tinkoffinvestementbot.exception.chat.ProcessingException;
import com.itmo.tinkoffinvestementbot.model.dto.trading.TempTradingOrderDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TempTradingOrderServiceImpl implements TempTradingOrderService {

    private final static Map<Long, TempTradingOrderDto> tempStorage = new HashMap<>();

    @Override
    public void create(
            Long userId, Long strategyId, Map<String, String> additionalParamNames
    ) {
        tempStorage.put(userId, new TempTradingOrderDto(strategyId, additionalParamNames));
    }

    @Override
    public TempTradingOrderDto get(Long userId) {
        TempTradingOrderDto orderDto = tempStorage.get(userId);
        if (orderDto == null)
            throw new ProcessingException(userId, "Разработчики трудились всю ночь и на этом моменте уснули");
        return orderDto;
    }
}
