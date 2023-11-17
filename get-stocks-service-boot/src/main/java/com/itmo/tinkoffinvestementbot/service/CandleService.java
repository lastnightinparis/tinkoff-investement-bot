package com.itmo.tinkoffinvestementbot.service;


import ru.tinkoff.piapi.contract.v1.CandleInterval;
import tinkoffinvestementbot.dto.CandlesDto;

import java.time.format.DateTimeParseException;

public interface CandleService {
    CandlesDto getCandlesByTicker(String ticker, String startDate, String endDate, CandleInterval candleInterval) throws DateTimeParseException;

}
