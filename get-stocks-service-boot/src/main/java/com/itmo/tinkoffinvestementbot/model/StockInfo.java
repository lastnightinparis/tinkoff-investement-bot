package com.itmo.tinkoffinvestementbot.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class StockInfo {
    String ticker;
    String figi;
    String name;
    String type;
    String currency;
}
