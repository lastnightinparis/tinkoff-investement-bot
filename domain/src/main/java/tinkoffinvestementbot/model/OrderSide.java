package tinkoffinvestementbot.model;

import ru.tinkoff.piapi.contract.v1.OrderDirection;

import java.util.Map;

public enum OrderSide {
    BUY, SELL, HOLD;
    private static final Map<OrderSide, OrderDirection> SIDE_TO_DIRECTION = Map.of(BUY, OrderDirection.ORDER_DIRECTION_BUY,
            SELL, OrderDirection.ORDER_DIRECTION_SELL,
            HOLD, OrderDirection.ORDER_DIRECTION_UNSPECIFIED);

    public OrderDirection getDirection() {
        return SIDE_TO_DIRECTION.get(this);
    }
}
