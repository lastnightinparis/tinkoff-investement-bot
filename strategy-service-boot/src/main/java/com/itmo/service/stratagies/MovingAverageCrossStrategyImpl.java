package com.itmo.service.stratagies;

import jdk.jfr.Description;
import tinkoffinvestementbot.dto.strategies.TradeSignal;
import tinkoffinvestementbot.model.strategies.TradeEvent;

import java.util.List;

public class MovingAverageCrossStrategyImpl implements AbstractTradeStrategy {
    private static final int SHORT_WINDOW = 3;
    private static final int LONG_WINDOW = 5;

    @Description("Количество акций, которое мы в данный момент держим")
    private long currentPositionQuantity;
    @Description("флаг, указывающий, что мы уже в позиции")
    private boolean currentlyInPosition = false;
    @Description("Тикер акции")
    private String stockSymbol;
    private final double totalCapital;
    private final double riskPerTrade;
    private List<Double> stockData;

    public MovingAverageCrossStrategyImpl(long currentPositionQuantity,
                                          List<Double> stockData,
                                          boolean currentlyInPosition,
                                          String stockSymbol,
                                          double totalCapital,
                                          double riskPerTrade
    ) {
        this.currentPositionQuantity = currentPositionQuantity;
        this.stockData = stockData;
        this.currentlyInPosition = currentlyInPosition;
        this.stockSymbol = stockSymbol;
        this.totalCapital = totalCapital;
        this.riskPerTrade = riskPerTrade;
    }

    @Override
    public TradeSignal checkForExit() {
        double shortSMA = calculateSMA(stockData, SHORT_WINDOW);
        double longSMA = calculateSMA(stockData, LONG_WINDOW);

        if (shortSMA < longSMA && currentlyInPosition) {
            long quantity = currentPositionQuantity;
            currentlyInPosition = false;
            return new TradeSignal(TradeEvent.SELL, stockSymbol, quantity);
        }
        return new TradeSignal(TradeEvent.HOLD, stockSymbol, currentPositionQuantity);
    }

    @Override
    public TradeSignal checkForEntry() {
        double shortSMA = calculateSMA(stockData, SHORT_WINDOW);
        double longSMA = calculateSMA(stockData, LONG_WINDOW);

        if (shortSMA > longSMA && !currentlyInPosition) {
            double stockPrice = stockData.get(stockData.size() - 1);
            long quantity = calculateQuantity(stockPrice);
            currentlyInPosition = true;
            return new TradeSignal(TradeEvent.BUY, stockSymbol, quantity);
        }
        return new TradeSignal(TradeEvent.HOLD, stockSymbol, currentPositionQuantity);
    }

    private long calculateQuantity(double stockPrice) {
        double capitalAtRisk = totalCapital * riskPerTrade; // Капитал под риском
        return (long) (capitalAtRisk / stockPrice);
    }

    private double calculateSMA(List<Double> values, int window) {
        if (values == null || values.size() < window) {
            throw new IllegalArgumentException("Список значений слишком мал для расчета SMA");
        }
        double sum = 0;
        for (int i = values.size() - window; i < values.size(); i++) {
            sum += values.get(i);
        }
        return sum / window;
    }
}
