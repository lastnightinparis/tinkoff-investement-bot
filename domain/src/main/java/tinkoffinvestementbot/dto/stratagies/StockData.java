package tinkoffinvestementbot.dto.stratagies;

import java.util.List;

public record StockData(
        // тикер
        String stockSymbol,
        // временной интервал
        TimePeriod period,

        // спискок цен
        List<Double> data
) {}
