package com.itmo.service;

import com.itmo.service.stratagies.AbstractTradeStrategy;
import com.itmo.service.stratagies.MeanRevertingPairsTradeStrategyImpl;
import com.itmo.service.stratagies.MovingAverageCrossStrategyImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tinkoffinvestementbot.dto.strategies.*;
import tinkoffinvestementbot.model.strategies.StrategyType;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeAlgorithmsService {
    public ResponseStrategyDto getTradePoints(RequestStrategyDto dto) {
        AbstractTradeStrategy strategy = null;
        TradeSignal enters;
        TradeSignal exits;

        if (dto.strategyName().equals(StrategyType.MAC)) {
            StrategyDto s = dto.strategy();

            StockData stockData = s.stockDatas().get(0);
            strategy = new MovingAverageCrossStrategyImpl(
                    s.currentPositionQuantity(),
                    s.shortWindow(),
                    s.longWindow(),
                    stockData.data(),
                    s.currentlyInPosition(),
                    stockData.stockSymbol(),
                    s.totalCapital(),
                    s.riskPerTrade()
            );
        } else if (dto.strategyName().equals(StrategyType.MRP)) {
            strategy = new MeanRevertingPairsTradeStrategyImpl();
        }
        if (strategy == null) {
            return new ResponseStrategyDto(null, null);
        }
        enters = strategy.checkForEntry();
        exits = strategy.checkForExit();
        return new ResponseStrategyDto(List.of(enters), List.of(exits));
    }
}
