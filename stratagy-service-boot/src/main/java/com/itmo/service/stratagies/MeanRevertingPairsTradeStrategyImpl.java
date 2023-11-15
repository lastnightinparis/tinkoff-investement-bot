package com.itmo.service.stratagies;

import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Description("""
        Выбор пар акций для стратегии средневозвращающей торговли зависит от многих факторов, включая корреляцию,
        коинтеграцию и ликвидность активов. В общем случае, для тестирования стратегии торговли парами акций,
        вы хотите выбрать акции, которые имеют историческую тенденцию двигаться вместе. Это обычно акции компаний
        в одной отрасли или схожего бизнеса.
        """)
@Service
@RequiredArgsConstructor
public class MeanRevertingPairsTradeStrategyImpl implements AbstractTradeStrategy {
    private String stock1Symbol;
    private String stock2Symbol;
    private List<Double> stock1Data;
    private List<Double> stock2Data;
    private double entryThreshold;
    private double exitThreshold;
    private Long quantity;

    @Override
    public TradeSignal checkForEntry() {
        if (stock1Data.size() != stock2Data.size() || stock1Data.isEmpty()) {
            throw new IllegalStateException("Stock data lists must be of the same size and not empty");
        }

        // Расчет спреда между двумя акциями
        List<Double> spread = IntStream.range(0, stock1Data.size())
                .mapToDouble(i -> stock1Data.get(i) - stock2Data.get(i))
                .boxed()
                .toList();

        // Расчет среднего спреда и стандартного отклонения
        double meanSpread = spread.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double stdDev = calculateStandardDeviation(spread, meanSpread);

        // Последний известный спред
        double lastSpread = spread.get(spread.size() - 1);

        // Проверка условия входа
        if (Math.abs(lastSpread - meanSpread) > entryThreshold * stdDev) {
            // Решение о покупке или продаже будет зависеть от того, какая акция недооценена, а какая переоценена
            String action = lastSpread > meanSpread ? "SELL" : "BUY";
            String stockSymbol = lastSpread > meanSpread ? stock1Symbol : stock2Symbol;
            quantity = 100L;
            return new TradeSignal(action, stockSymbol, quantity); // Пример количества для сигнала, может быть любым
        }

        return null; // держать
    }

    @Override
    public TradeSignal checkForExit() {
        if (stock1Data.size() != stock2Data.size() || stock1Data.isEmpty()) {
            throw new IllegalStateException("Stock data lists must be of the same size and not empty");
        }

        // Расчет спреда между двумя акциями
        List<Double> spread = IntStream.range(0, stock1Data.size())
                .mapToDouble(i -> stock1Data.get(i) - stock2Data.get(i))
                .boxed()
                .toList();

        // Расчет среднего спреда
        double meanSpread = spread.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        // Последний известный спред
        double lastSpread = spread.get(spread.size() - 1);

        // Проверка условия выхода
        // Условие выхода обычно менее строгое, чем условие входа, что позволяет избежать ложных сигналов
        if (Math.abs(lastSpread - meanSpread) < exitThreshold) {
            // Сигнал на продажу или покупку зависит от первоначального сигнала входа
            String action = "SELL"; // Это предполагает, что первоначальный вход был на покупку
            String stockSymbol = stock1Symbol; // или stock2Symbol, в зависимости от ситуации
            return new TradeSignal(action, stockSymbol, quantity);
        }

        return null; // держать
    }

    public void setStockPair(String stock1Symbol, String stock2Symbol) {
        this.stock1Symbol = stock1Symbol;
        this.stock2Symbol = stock2Symbol;
    }

    @Override
    public void setHistoricalData(List<Double> stock1Data, List<Double> stock2Data) {
        this.stock1Data = stock1Data;
        this.stock2Data = stock2Data;
    }

    @Override
    public void setEntryThreshold(double entryThreshold) {
        this.entryThreshold = entryThreshold;
    }

    @Override
    public void setExitThreshold(double exitThreshold) {
        this.exitThreshold = exitThreshold;
    }

    private double calculateStandardDeviation(List<Double> values, double mean) {
        double variance = values.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .sum() / values.size();
        return Math.sqrt(variance);
    }
}
