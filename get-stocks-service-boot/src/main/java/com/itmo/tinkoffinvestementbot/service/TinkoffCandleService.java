package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.config.ApiConfig;
import com.itmo.tinkoffinvestementbot.dto.CandlesDto;
import com.itmo.tinkoffinvestementbot.model.Candle;
import com.itmo.tinkoffinvestementbot.model.StockInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static ru.tinkoff.piapi.core.utils.DateUtils.timestampToString;
import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TinkoffCandleService implements CandleService {

    @Autowired
    private InvestApi api;

    public List<CandlesDto> getCandlesByTicker(String ticker) {

        //Поиск инструмента по ticker (в результатах поиска нет опционов)
        api.getInstrumentsService().findInstrumentSync(ticker);

        //Получаем базовые списки инструментов и печатаем их
        List<Share> shares = api.getInstrumentsService().getTradableSharesSync().subList(0, 10);
//        var etfs = api.getInstrumentsService().getTradableEtfsSync();
//        var bonds = api.getInstrumentsService().getTradableBondsSync();
//        var futures = api.getInstrumentsService().getTradableFuturesSync();
//        var currencies = api.getInstrumentsService().getTradableCurrenciesSync();

        return shares.stream().
                map(this::getStockModel)
                .map(stockInfo -> new CandlesDto(stockInfo, getCandlesExample(stockInfo.getFigi())))
                .toList();

    }

    private List<Candle> getCandlesExample(String figi) {
        //Получаем и печатаем список свечей для инструмента

        List<HistoricCandle> candles1Hour = api.getMarketDataService()
                .getCandlesSync(figi, Instant.now().minus(1, ChronoUnit.DAYS), Instant.now(),
                        CandleInterval.CANDLE_INTERVAL_HOUR);

        if (!candles1Hour.isEmpty()) {
            log.info("получено {} 1-часовых свечей для инструмента с figi {}", candles1Hour.size(), figi);
            return candles1Hour.stream().map(this::getCandleModel).toList();
        }
        return Collections.emptyList();
    }

    private Candle getCandleModel(HistoricCandle candle) {
        var open = quotationToBigDecimal(candle.getOpen());
        var close = quotationToBigDecimal(candle.getClose());
        var high = quotationToBigDecimal(candle.getHigh());
        var low = quotationToBigDecimal(candle.getLow());
        var volume = candle.getVolume();
        var time = timestampToString(candle.getTime());
        log.info(
                "цена открытия: {}, цена закрытия: {}, минимальная цена за 1 лот: {}, максимальная цена за 1 лот: {}, объем " +
                        "торгов в лотах: {}, время свечи: {}",
                open, close, low, high, volume, time);
        return new Candle(open, close, high, low, volume, time);
    }

    private StockInfo getStockModel(Share share) {
        return new StockInfo(share.getTicker(), share.getFigi(), share.getName(), share.getShareType().name(), share.getCurrency());
    }

}
