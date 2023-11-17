package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.repository.StrategyRepository;
import com.itmo.tinkoffinvestementbot.service.client.StockServiceClient;
import com.itmo.tinkoffinvestementbot.service.client.StrategyServiceClient;
import com.itmo.tinkoffinvestementbot.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tinkoffinvestementbot.dto.CandlesDto;
import tinkoffinvestementbot.dto.EventDto;
import tinkoffinvestementbot.model.EventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@SuppressWarnings("rawusage")
public class TransmitterServiceImpl implements TransmitterService {
    private final StrategyRepository strategyRepository;
    private final StockServiceClient stockServiceClient;
    private final StrategyServiceClient strategyServiceClient;
    private final OrderService orderService;

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    @Override
    @Scheduled(fixedDelay = 1L, timeUnit = TimeUnit.MINUTES)
    public void transmit() {
        val tickers = strategyRepository.getTickers();
        val quoteFutures = new ArrayList<CompletableFuture<CandlesDto>>(tickers.size());
        tickers.forEach(t -> quoteFutures.add(CompletableFuture.supplyAsync(() -> stockServiceClient.getCandles(t), scheduledExecutorService)));
        var quoteFuturesArray = quoteFutures.toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(quoteFuturesArray).join();

        Arrays.stream(quoteFuturesArray).parallel()
                .forEach(cf -> {
                    try {
                        var candles = (CandlesDto) cf.get();
                        val orderDto = strategyServiceClient.fireEvent(new EventDto(EventType.CANDLE, "ticker", List.of(candles))); // FIXME
                        if (orderDto != null) {
                            orderService.sendOrder(orderDto);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
