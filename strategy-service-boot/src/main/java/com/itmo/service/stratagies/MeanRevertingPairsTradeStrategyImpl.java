package com.itmo.service.stratagies;

import com.itmo.dto.TradeEvent;
import com.itmo.dto.TradeSignal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeanRevertingPairsTradeStrategyImpl implements AbstractTradeStrategy {
    private String stock1Symbol;
    private String stock2Symbol;
    private List<Double> stock1Data;
    private List<Double> stock2Data;
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
                .collect(Collectors.toList());

        // Расчет среднего спреда и стандартного отклонения
        double meanSpread = spread.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double stdDev = calculateStandardDeviation(spread, meanSpread);

        // Последний известный спред
        double lastSpread = spread.get(spread.size() - 1);

        // Проверка условия входа
        if (Math.abs(lastSpread - meanSpread) > entryThreshold * stdDev) {
            // Решение о покупке или продаже будет зависеть от того, какая акция недооценена, а какая переоценена
            String stockSymbol = lastSpread > meanSpread ? stock1Symbol : stock2Symbol;
            quantity = 100L;
            return new TradeSignal(TradeEvent.BUY, stockSymbol, quantity); // Пример количества для сигнала, может быть любым
        }

        return new TradeSignal(TradeEvent.HOLD, null, 0L);
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
                .collect(Collectors.toList());

        // Расчет среднего спреда
        double meanSpread = spread.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        // Последний известный спред
        double lastSpread = spread.get(spread.size() - 1);

        // Проверка условия выхода
        // Условие выхода обычно менее строгое, чем условие входа, что позволяет избежать ложных сигналов
        if (Math.abs(lastSpread - meanSpread) < exitThreshold) {
            // Сигнал на продажу или покупку зависит от первоначального сигнала входа
            String stockSymbol = stock1Symbol; // или stock2Symbol, в зависимости от ситуации
            return new TradeSignal(TradeEvent.SELL, stockSymbol, quantity);
        }

        return new TradeSignal(TradeEvent.HOLD, null, 0L);
    }

    private double calculateStandardDeviation(List<Double> values, double mean) {
        double variance = values.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .sum() / values.size();
        return Math.sqrt(variance);
    }
}
