package com.itmo.tinkoffinvestementbot.controller;


import com.itmo.tinkoffinvestementbot.dto.FigiesDto;
import com.itmo.tinkoffinvestementbot.dto.StocksPricesDto;
import com.itmo.tinkoffinvestementbot.model.Stock;
import com.itmo.tinkoffinvestementbot.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class StockController {
    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }

    @PostMapping("/prices")
    public StocksPricesDto getPricesStocksByFigies(@RequestBody FigiesDto figiesDto) {
        return stockService.getPricesStocksByFigies(figiesDto);
    }

}
