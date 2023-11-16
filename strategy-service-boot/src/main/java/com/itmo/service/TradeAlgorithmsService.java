package com.itmo.service;

import com.itmo.dto.RequestStrategyDto;
import com.itmo.dto.ResponseStrategyDto;
import com.itmo.dto.StrategyType;
import com.itmo.dto.TradeSignal;
import com.itmo.service.stratagies.MeanRevertingPairsTradeStrategyImpl;
import com.itmo.service.stratagies.MovingAverageCrossStrategyImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeAlgorithmsService {
    private final MovingAverageCrossStrategyImpl movingAverageCrossStrategy;
    private final MeanRevertingPairsTradeStrategyImpl meanRevertingPairsTradeStrategy;

    public ResponseStrategyDto getTradePoints(RequestStrategyDto dto) {
        if (dto.strategyType().equals(StrategyType.MAC)) {
            TradeSignal enters = movingAverageCrossStrategy.checkForEntry();
            TradeSignal exits = movingAverageCrossStrategy.checkForExit();
            return new ResponseStrategyDto(List.of(enters), List.of(exits));

        } else if (dto.strategyType().equals(StrategyType.MRP)) {
            TradeSignal enters = meanRevertingPairsTradeStrategy.checkForEntry();
            TradeSignal exits = meanRevertingPairsTradeStrategy.checkForExit();
            return new ResponseStrategyDto(List.of(enters), List.of(exits));
        }
        return new ResponseStrategyDto(List.of(), List.of());
    }
}
