package com.itmo.tinkoffinvestementbot.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.InstrumentShort;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoffinvestementbot.dto.CandlesDto;
import tinkoffinvestementbot.model.Candle;
import tinkoffinvestementbot.model.InstrumentInfo;

import java.time.Instant;
import java.time.format.DateTimeParseException;
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

    public CandlesDto getCandlesByTicker(String ticker, String startDate, String endDate, CandleInterval candleInterval) throws DateTimeParseException {

        val optionalInstrument = api.getInstrumentsService().findInstrumentSync(ticker)
                .stream()
                .filter(instrumentShort -> instrumentShort.getTicker().equals(ticker)
                        && instrumentShort.getClassCode().startsWith("T"))
                .findFirst();

        if (optionalInstrument.isEmpty()) {
            return null;
        }

        return optionalInstrument
                .map(this::getStockModel)
                .map(instrumentInfo -> new CandlesDto(instrumentInfo, getCandlesByFigi(instrumentInfo.figi(), Instant.parse(startDate), Instant.parse(endDate), candleInterval)))
                .get();

    }

    private List<Candle> getCandlesByFigi(String figi, Instant from, Instant to, CandleInterval candleInterval) {

        List<HistoricCandle> candles = api.getMarketDataService()
                .getCandlesSync(figi, from, to, candleInterval);

        if (!candles.isEmpty()) {
            log.info("{} candles were found for instrument with figi={}", candles.size(), figi);
            return candles.stream().map(this::getCandleModel).toList();
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
        return new Candle(open, close, high, low, volume, time);
    }

    private InstrumentInfo getStockModel(InstrumentShort instrument) {
        return new InstrumentInfo(instrument.getTicker(), instrument.getFigi(), instrument.getName(), instrument.getInstrumentType());
    }

}
