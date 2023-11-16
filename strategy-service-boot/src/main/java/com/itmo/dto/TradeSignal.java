package com.itmo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class TradeSignal {
    TradeEvent tradeEvent;
    String stockSymbol;
    Long quantity;
}
