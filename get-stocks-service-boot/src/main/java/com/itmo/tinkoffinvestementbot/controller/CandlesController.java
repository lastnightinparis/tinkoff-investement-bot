package com.itmo.tinkoffinvestementbot.controller;


import com.itmo.tinkoffinvestementbot.service.CandleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import tinkoffinvestementbot.dto.CandlesDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@RestController
public class CandlesController {
    private final CandleService candleService;

    @GetMapping("/currentCandles/{ticker}")
    public CandlesDto getStock(@PathVariable String ticker) {
        return candleService.getCandlesByTicker(ticker, Instant.now().minus(1, ChronoUnit.DAYS).toString(), Instant.now().toString(), CandleInterval.CANDLE_INTERVAL_2_HOUR);

    }

    @GetMapping("/customCandles")
    public CandlesDto getStockByTickerAndDates(@RequestParam String ticker, @RequestParam String startDate, @RequestParam String endDate, @RequestParam String intervalMin) {
        return candleService.getCandlesByTicker(ticker, startDate, endDate,
                switch (intervalMin) {
                    case "1" -> CandleInterval.CANDLE_INTERVAL_1_MIN;
                    case "5" -> CandleInterval.CANDLE_INTERVAL_5_MIN;
                    case "15" -> CandleInterval.CANDLE_INTERVAL_15_MIN;
                    case "30" -> CandleInterval.CANDLE_INTERVAL_30_MIN;
                    default -> CandleInterval.CANDLE_INTERVAL_HOUR;
                });
    }

}
