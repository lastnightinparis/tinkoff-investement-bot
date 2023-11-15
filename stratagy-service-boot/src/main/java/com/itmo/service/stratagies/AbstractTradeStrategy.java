package com.itmo.service.stratagies;

import java.util.List;

public interface AbstractTradeStrategy {
    /**
     * Sets the historical price data for the stocks.
     *
     * @param stock1Data the historical price data for the first stock
     * @param stock2Data the historical price data for the second stock
     */
    void setHistoricalData(List<Double> stock1Data, List<Double> stock2Data);

    /**
     * Defines the entry threshold for the strategy.
     *
     * @param entryThreshold the threshold to enter the trade
     */
    void setEntryThreshold(double entryThreshold);

    /**
     * Defines the exit threshold for the strategy.
     *
     * @param exitThreshold the threshold to exit the trade
     */
    void setExitThreshold(double exitThreshold);

    /**
     * Analyzes the current market data and determines if there is an
     * opportunity to enter a trade based on the mean-reverting relationship
     * between the pair of stocks.
     *
     * @return an entry signal if the conditions are met, otherwise null
     */
    TradeSignal checkForEntry();

    /**
     * Analyzes the current market data and determines if the conditions
     * to exit a previously entered trade are met.
     *
     * @return an exit signal if the conditions are met, otherwise null
     */
    TradeSignal checkForExit();

    /**
     * Represents a trade signal, indicating the action to be taken, the stock symbol,
     * and the quantity for the trade.
     */
    class TradeSignal {
        private final String action; // "BUY" or "SELL"
        private final String stockSymbol;
        private final Long quantity;

        public TradeSignal(String action, String stockSymbol, Long quantity) {
            this.action = action;
            this.stockSymbol = stockSymbol;
            this.quantity = quantity;
        }

        public String getAction() {
            return action;
        }

        public String getStockSymbol() {
            return stockSymbol;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}

