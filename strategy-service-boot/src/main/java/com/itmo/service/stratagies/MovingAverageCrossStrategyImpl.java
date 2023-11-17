package com.itmo.service.stratagies;

import jdk.jfr.Description;
import tinkoffinvestementbot.dto.stratagies.TradeSignal;
import tinkoffinvestementbot.model.strategies.TradeEvent;

import java.util.List;

public class MovingAverageCrossStrategyImpl implements AbstractTradeStrategy {
    @Description("Количество акций, которое мы в данный момент держим")
    private long currentPositionQuantity;
    private int shortWindow = 3;
    private int longWindow = 5;
    private List<Double> stockData;
    @Description("флаг, указывающий, что мы уже в позиции")
    private boolean currentlyInPosition = false;
    @Description("Тикер акции")
    private String stockSymbol;
    private double totalCapital;
    private double riskPerTrade = 0.02;

    public MovingAverageCrossStrategyImpl(long currentPositionQuantity,
                                          int shortWindow,
                                          int longWindow,
                                          List<Double> stockData,
                                          boolean currentlyInPosition,
                                          String stockSymbol,
                                          double totalCapital,
                                          double riskPerTrade
    ) {
        this.currentPositionQuantity = currentPositionQuantity;
        this.shortWindow = shortWindow;
        this.longWindow = longWindow;
        this.stockData = stockData;
        this.currentlyInPosition = currentlyInPosition;
        this.stockSymbol = stockSymbol;
        this.totalCapital = totalCapital;
        this.riskPerTrade = riskPerTrade;
    }

    @Override
    public TradeSignal checkForExit() {
        double shortSMA = calculateSMA(stockData, shortWindow);
        double longSMA = calculateSMA(stockData, longWindow);

        if (shortSMA < longSMA && currentlyInPosition) {
            long quantity = currentPositionQuantity;
            currentlyInPosition = false;
            return new TradeSignal(TradeEvent.SELL, stockSymbol, quantity);
        }
        return null;
    }

    @Override
    public TradeSignal checkForEntry() {
        double shortSMA = calculateSMA(stockData, shortWindow);
        double longSMA = calculateSMA(stockData, longWindow);

        if (shortSMA > longSMA && !currentlyInPosition) {
            double stockPrice = stockData.get(stockData.size());
            long quantity = calculateQuantity(stockPrice);
            currentlyInPosition = true;
            return new TradeSignal(TradeEvent.BUY, stockSymbol, quantity);
        }
        return new TradeSignal(TradeEvent.HOLD, stockSymbol, 0L);
    }

    private long calculateQuantity(double stockPrice) {
        double capitalAtRisk = totalCapital * riskPerTrade; // Капитал под риском
        long quantity = (long) (capitalAtRisk / stockPrice);
        return quantity;
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
