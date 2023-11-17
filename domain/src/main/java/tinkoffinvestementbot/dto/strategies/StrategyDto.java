package tinkoffinvestementbot.dto.strategies;

import java.util.List;

public record StrategyDto(
        long currentPositionQuantity,
        List<StockData> stockDatas,
        boolean currentlyInPosition,
        double stockPrice,
        double totalCapital,
        double riskPerTrade
){
}
