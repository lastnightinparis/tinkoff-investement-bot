package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.service.client.OrderServiceClient;
import com.itmo.tinkoffinvestementbot.service.client.StockServiceClient;
import com.itmo.tinkoffinvestementbot.service.client.StrategyServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tinkoffinvestementbot.dto.CandlesDto;
import tinkoffinvestementbot.dto.EventDto;
import tinkoffinvestementbot.dto.OrderDto;
import tinkoffinvestementbot.model.EventType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransmitterServiceImpl implements TransmitterService {
    private final StockServiceClient stockServiceClient;
    private final StrategyServiceClient strategyServiceClient;
    private final OrderServiceClient orderServiceClient;

    @Override
    public void transmit(String ticker) {
        final List<CandlesDto> candles = stockServiceClient.getCandles(ticker);
        final OrderDto orderDto = strategyServiceClient.fireEvent(new EventDto(EventType.CANDLE, ticker, candles));
        if (orderDto != null) {
            orderServiceClient.registerOrder(orderDto);
        }
    }
}
