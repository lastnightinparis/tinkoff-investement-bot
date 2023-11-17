package com.itmo.service;

import com.itmo.service.stratagies.AbstractTradeStrategy;
import com.itmo.service.stratagies.MeanRevertingPairsTradeStrategyImpl;
import com.itmo.service.stratagies.MovingAverageCrossStrategyImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tinkoffinvestementbot.dto.stratagies.RequestStrategyDto;
import tinkoffinvestementbot.dto.stratagies.ResponseStrategyDto;
import tinkoffinvestementbot.dto.stratagies.StockData;
import tinkoffinvestementbot.dto.stratagies.StrategyDto;
import tinkoffinvestementbot.dto.stratagies.TradeSignal;
import tinkoffinvestementbot.model.strategies.StratagyType;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeAlgorithmsService {
    public ResponseStrategyDto getTradePoints(RequestStrategyDto dto) {
        AbstractTradeStrategy strategy = null;
        TradeSignal enters;
        TradeSignal exits;

        if (dto.stratagyName().equals(StratagyType.MAC)) {
            StrategyDto s = dto.strategy();

            StockData stockData = s.stockDatas().get(0);
            strategy = new MovingAverageCrossStrategyImpl(s.currentPositionQuantity(),
                    s.shortWindow(),
                    s.longWindow(),
                    stockData.data(),
                    s.currentlyInPosition(),
                    stockData.stockSymbol(),
                    s.totalCapital(),
                    s.riskPerTrade()
            );
        } else if (dto.stratagyName().equals(StratagyType.MRP)) {
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
