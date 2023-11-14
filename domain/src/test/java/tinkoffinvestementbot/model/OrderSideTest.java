package tinkoffinvestementbot.model;

import org.junit.jupiter.api.Test;
import ru.tinkoff.piapi.contract.v1.OrderDirection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderSideTest {
    @Test
    void getDirection() {
        assertEquals(OrderDirection.ORDER_DIRECTION_BUY, OrderSide.BUY.getDirection());
    }
}