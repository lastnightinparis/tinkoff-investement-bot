package com.itmo.tinkoffinvestementbot.service.order.sandbox;

import com.itmo.tinkoffinvestementbot.repository.TradeOrderRepository;
import com.itmo.tinkoffinvestementbot.service.order.OrderStatusChangeObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
class OrderStatusChangeSandboxObserverImplIT {

    @Autowired
    private OrderStatusChangeObserver observer;

    @SpyBean
    private TradeOrderRepository tradeOrderRepository;

    @Test
    void checkOrdersStatus() {
        observer.checkOrdersStatus();

        verify(tradeOrderRepository).getNewOrders();
    }

}