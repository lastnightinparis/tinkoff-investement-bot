package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.dto.FigiesDto;
import com.itmo.tinkoffinvestementbot.dto.StocksPricesDto;
import com.itmo.tinkoffinvestementbot.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto);
}
