package com.itmo.service.stratagies;

import com.itmo.model.Bars;
import com.itmo.model.Event;
import com.itmo.model.EventQueue;
import tinkoffinvestementbot.dto.stratagies.TradeSignal;
import tinkoffinvestementbot.model.strategies.TradeEvent;
import jdk.jfr.Description;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Description("Это тот же MovingAverageCrossStrategy только в общем виде")
public class MACStrategy {
    private int shortWindow;
    private int longWindow;
    private Map<String, String> bought;
    private String symbol;
    private Bars bars;
    private EventQueue events;  // EventQueue представляет собой очередь для событий


    public TradeSignal calculateSignals(Event event) {
        if (event.equals(Event.MARKET)) {
                List<Double> barValues = bars.getLatestBarValues(symbol, "adj_close", longWindow);
                Date barDate = bars.getLatestBarDateTime(symbol);

                if (barValues != null && !barValues.isEmpty()) {
                    double shortSMA = calculateSMA(barValues, shortWindow);
                    double longSMA = calculateSMA(barValues, longWindow);

                    if (shortSMA > longSMA && bought.get(symbol).equals("OUT")) {
                        System.out.println("LONG: " + barDate);
                        TradeSignal signal = new TradeSignal(TradeEvent.LONG, symbol, 1L);
                        events.put(signal);
                        bought.put(symbol, "LONG");
                    } else if (shortSMA < longSMA && bought.get(symbol).equals("LONG")) {
                        System.out.println("SHORT: " + barDate);
                        TradeSignal signal = new TradeSignal(TradeEvent.EXIT, symbol, 1L);
                        events.put(signal);
                        bought.put(symbol, "OUT");
                    }
                }
        }
        return new TradeSignal(TradeEvent.HOLD, symbol, 1L);
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
