package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.dto.FigiesDto;
import com.itmo.tinkoffinvestementbot.dto.StocksPricesDto;
import com.itmo.tinkoffinvestementbot.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;


@Slf4j
@Service
@RequiredArgsConstructor
public class TinkoffStockService implements StockService {


    public Stock getStockByTicker(String ticker) {
        return null;
    }


    public StocksPricesDto getPricesStocksByFigies(FigiesDto figiesDto) {
        return null;

    }

}
