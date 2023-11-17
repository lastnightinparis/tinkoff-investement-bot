package tinkoffinvestementbot.dto.stratagies;

import java.util.List;

public record StrategyDto(
        long currentPositionQuantity,
        int shortWindow,
        int longWindow,
        List<StockData> stockDatas,
        boolean currentlyInPosition,
        double stockPrice,
        double totalCapital,
        double riskPerTrade
){
}