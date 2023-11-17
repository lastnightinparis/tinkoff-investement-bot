package tinkoffinvestementbot.dto.strategies;

import jdk.jfr.Description;
import tinkoffinvestementbot.model.strategies.TradeEvent;

@Description("""
         Represents a trade signal, indicating the action to be taken, the stock symbol,
         and the quantity for the trade.
        """)
public record TradeSignal(
        TradeEvent action,
        String stockSymbol,
        Long quantity
) {
}
