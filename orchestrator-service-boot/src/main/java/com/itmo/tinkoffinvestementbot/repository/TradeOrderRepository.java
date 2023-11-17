package com.itmo.tinkoffinvestementbot.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tinkoffinvestementbot.model.OrderResult;
import tinkoffinvestementbot.model.TinkoffUser;
import tinkoffinvestementbot.model.TradeOrder;

import java.util.List;

@Repository
public interface TradeOrderRepository extends JpaRepository<TradeOrder, String> {

    @Transactional
    default TradeOrder save(OrderResult orderResult, TinkoffUser tinkoffUser) {
        return this.save(TradeOrder.builder()
                .id(orderResult.id())
                .status(orderResult.status())
                .tinkoffUser(tinkoffUser)
                .build());
    }

    @Query(value = "select o from TradeOrder o where o.status = 'CREATED'")
    List<TradeOrder> getNewOrders();
}
