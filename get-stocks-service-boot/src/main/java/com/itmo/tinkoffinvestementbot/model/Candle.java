package com.itmo.tinkoffinvestementbot.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
public class Candle {
    BigDecimal openPrice;
    BigDecimal closePrice;
    BigDecimal minPer1Batch;
    BigDecimal maxPer1Batch;
    long volume;
    String time;
}
