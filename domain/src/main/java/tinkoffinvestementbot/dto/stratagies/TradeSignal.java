package tinkoffinvestementbot.dto.stratagies;

import jdk.jfr.Description;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
