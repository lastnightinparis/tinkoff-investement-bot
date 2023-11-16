package com.itmo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class ResponseStrategyDto {
    List<TradeSignal> enterPoints;
    List<TradeSignal> exitPoints;
}
