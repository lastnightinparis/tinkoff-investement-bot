package com.itmo.tinkoffinvestementbot.controller;


import com.itmo.tinkoffinvestementbot.dto.CandlesDto;
import com.itmo.tinkoffinvestementbot.service.CandleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CandlesController {
    private final CandleService candleService;

    @GetMapping("/candles/{ticker}")
    public List<CandlesDto> getStock(@PathVariable String ticker) {
        return candleService.getCandlesByTicker(ticker);
    }

}
