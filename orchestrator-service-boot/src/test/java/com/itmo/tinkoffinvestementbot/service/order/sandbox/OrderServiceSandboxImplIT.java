package com.itmo.tinkoffinvestementbot.service.order.sandbox;

import com.itmo.tinkoffinvestementbot.service.order.OrderService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tinkoffinvestementbot.dto.OrderDto;
import tinkoffinvestementbot.model.OrderSide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tinkoffinvestementbot.model.OrderStatus.CREATED;
import static tinkoffinvestementbot.model.OrderStatus.WONT_EXECUTE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
class OrderServiceSandboxImplIT {
    private static final Long USER_ID = 42069L;

    @Autowired
    private OrderService orderService;

    @Test
    void postBuyOrder() {
        val result = orderService.sendOrder(new OrderDto(USER_ID, "BBG006L8G4H1", 1L, OrderSide.BUY));
        assertEquals(CREATED, result.status());
    }

    @Test
    void postSellOrder() {
        val result = orderService.sendOrder(new OrderDto(USER_ID, "BBG006L8G4H1", 1L, OrderSide.SELL));
        assertEquals(CREATED, result.status());
    }

    @Test
    void postHoldOrder() {
        val result = orderService.sendOrder(new OrderDto(USER_ID, "BBG006L8G4H1", 1L, OrderSide.HOLD));
        assertEquals(WONT_EXECUTE, result.status());
    }

}