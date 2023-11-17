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
import tinkoffinvestementbot.dto.OrderDto;
import tinkoffinvestementbot.dto.strategies.RequestStrategyDto;
import tinkoffinvestementbot.dto.strategies.StockData;
import tinkoffinvestementbot.dto.strategies.StrategyDto;
import tinkoffinvestementbot.dto.strategies.TimePeriod;
import tinkoffinvestementbot.model.OrderSide;
import tinkoffinvestementbot.model.strategies.TradeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

        val quotesByTicker = Arrays.stream(quoteFuturesArray)
                .map(cf -> {
                    try {
                        return (CandlesDto) cf.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(candlesDto -> candlesDto.instrumentInfo().ticker(), candlesDto -> candlesDto));

        val strategies = strategyRepository.getActiveStrategies();
        for (var strategy : strategies) {
            val quotes = quotesByTicker.get(strategy.ticker());
            val candles = quotes.candles();
            val strategyResolution = strategyServiceClient.getInfo(
                    new RequestStrategyDto(strategy.type(),
                            new StrategyDto(
                                    100,
                                    List.of(new StockData(strategy.ticker(), TimePeriod.ONE_HOUR, candles.stream().map(candle -> candle.openPrice().add(candle.closePrice()).doubleValue() / 2.0).collect(Collectors.toList()))),
                                    true,
                                    candles.get(0).closePrice().doubleValue(),
                                    10_000.0,
                                    strategy.riskRating()
                            )));

            val enterAction = strategyResolution.enterPoints().stream().findFirst();
            if (enterAction.isPresent()) {
                var action = enterAction.get();
                if (action.action() == TradeEvent.BUY) {
                    orderService.sendOrder(new OrderDto(strategy.tinkoffUser().id(), quotes.instrumentInfo().figi(), action.quantity(), OrderSide.BUY));
                    return;
                }
            }

            val exitAction = strategyResolution.exitPoints().stream().findFirst();
            if (exitAction.isPresent()) {
                var action = exitAction.get();
                if (action.action() == TradeEvent.SELL) {
                    orderService.sendOrder(new OrderDto(strategy.tinkoffUser().id(), quotes.instrumentInfo().figi(), action.quantity(), OrderSide.SELL));
                    return;
                }
            }
        }


    }
}
