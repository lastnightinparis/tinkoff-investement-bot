package com.itmo.service.stratagies;

import tinkoffinvestementbot.dto.strategies.TradeSignal;

public interface AbstractTradeStrategy {
    double entryThreshold = 10;
    double exitThreshold = 3;

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
}

