package com.itmo.service;

import com.itmo.service.stratagies.MeanRevertingPairsTradeStrategyImpl;
import com.itmo.service.stratagies.MovingAverageCrossStrategyImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import tinkoffinvestementbot.dto.stratagies.RequestStrategyDto;
import tinkoffinvestementbot.dto.stratagies.ResponseStrategyDto;
import tinkoffinvestementbot.dto.stratagies.TradeSignal;
import tinkoffinvestementbot.model.strategies.StratagyType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeAlgorithmsService {
    private final MovingAverageCrossStrategyImpl movingAverageCrossStrategy;
    private final MeanRevertingPairsTradeStrategyImpl meanRevertingPairsTradeStrategy;

    public ResponseStrategyDto getTradePoints(RequestStrategyDto dto) {
        if (dto.stratagyType().equals(StratagyType.MAC)) {
            TradeSignal enters = movingAverageCrossStrategy.checkForEntry();
            TradeSignal exits = movingAverageCrossStrategy.checkForExit();
            return new ResponseStrategyDto(List.of(enters), List.of(exits));

        } else if (dto.stratagyType().equals(StratagyType.MRP)) {
            TradeSignal enters = meanRevertingPairsTradeStrategy.checkForEntry();
            TradeSignal exits = meanRevertingPairsTradeStrategy.checkForExit();
            return new ResponseStrategyDto(List.of(enters), List.of(exits));
        }
        return new ResponseStrategyDto(List.of(), List.of());
    }
}
