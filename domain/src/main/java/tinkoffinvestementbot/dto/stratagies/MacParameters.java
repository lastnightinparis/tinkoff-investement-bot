package tinkoffinvestementbot.dto.stratagies;

public enum MacParameters {
    CURRENT_POSITION_QUANTITY("currentPositionQuantity"),
    SHORT_WINDOW("shortWindow"),
    LONG_WINDOW("longWindow"),
    STOCK_DATA("stockData"),
    CURRENTLY_IN_POSITION("currentlyInPosition"),
    STOCK_PRICE("stockPrice"),
    STOCK_SYMBOL("stockSymbol"),
    TOTAL_CAPITAL("totalCapital"),
    RISK_PER_TRADE("riskPerTrade");

    private final String fieldName;

    MacParameters(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

