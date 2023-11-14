package com.itmo.tinkoffinvestementbot.service.order;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tinkoffinvestementbot.dto.OrderDto;
import tinkoffinvestementbot.model.OrderResult;
import tinkoffinvestementbot.model.OrderSide;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
class SandboxOrderServiceImplIT {

    @Autowired
    private OrderService orderService;

    @Test
    void postBuyOrder() {
        val result = orderService.sendOrder(new OrderDto(1L, "BBG006L8G4H1", 1L, OrderSide.BUY));
        assertEquals(OrderResult.Status.CREATED, result.status());
    }

    @Test
    void postSellOrder() {
        val result = orderService.sendOrder(new OrderDto(1L, "BBG006L8G4H1", 1L, OrderSide.SELL));
        assertEquals(OrderResult.Status.CREATED, result.status());
    }

    @Test
    void postHoldOrder() {
        val result = orderService.sendOrder(new OrderDto(1L, "BBG006L8G4H1", 1L, OrderSide.HOLD));
        assertEquals(OrderResult.Status.WONT_EXECUTE, result.status());
    }

}