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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeAlgorithmsService {
    private List<String> statusMessages = new ArrayList<>();

    public ResponseStrategyDto getTradePoints(RequestStrategyDto dto) {
        AbstractTradeStrategy strategy = null;
        TradeSignal enters = null;
        TradeSignal exits = null;
        StrategyDto s = dto.strategy();

        try {
            if (dto.strategyName().equals(StratagyType.MAC)) {
                strategy = buildMovingAverageCrossStrategy(s);
            } else if (dto.strategyName().equals(StratagyType.MRP)) {
                strategy = buildMeanRevertingPairsTradeStrategy(s);
            }
            if (strategy == null) {
                statusMessages.add("Выбранная стратеги не существует либо не выбрана.");
            } else {
                enters = strategy.checkForEntry();
                exits = strategy.checkForExit();
            }

        } catch (Exception e) {
            statusMessages.add(e.getMessage());
        }

        return new ResponseStrategyDto(List.of(enters), List.of(exits), statusMessages);
    }

    private static AbstractTradeStrategy buildMeanRevertingPairsTradeStrategy(StrategyDto s) {
        AbstractTradeStrategy strategy;
        StockData stockData1 = s.stockDatas().get(0);
        StockData stockData2 = s.stockDatas().get(1);
        strategy = new MeanRevertingPairsTradeStrategyImpl(
                stockData1.data(),
                stockData2.data(),
                stockData1.stockSymbol(),
                stockData2.stockSymbol(),
                s.currentPositionQuantity()
        );
        return strategy;
    }

    private static AbstractTradeStrategy buildMovingAverageCrossStrategy(StrategyDto s) {
        AbstractTradeStrategy strategy;
        StockData stockData = s.stockDatas().get(0);
        strategy = new MovingAverageCrossStrategyImpl(
                s.currentPositionQuantity(),
                stockData.data(),
                s.currentlyInPosition(),
                stockData.stockSymbol(),
                s.totalCapital(),
                s.riskPerTrade()
        );
        return strategy;
    }
}
