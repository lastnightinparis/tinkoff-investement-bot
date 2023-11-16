package com.itmo.tinkoffinvestementbot.service.order;

import com.itmo.tinkoffinvestementbot.repository.TradeOrderRepository;
import com.itmo.tinkoffinvestementbot.service.client.OrderNotificationServiceClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoffinvestementbot.model.OrderStatus;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Profile("!dev")
@RequiredArgsConstructor
public class OrderStatusChangeObserverImpl implements OrderStatusChangeObserver {
    private final TradeOrderRepository tradeOrderRepository;
    private final OrderNotificationServiceClient notificationClient;

    @Transactional
    @Scheduled(fixedDelay = 30L, timeUnit = TimeUnit.SECONDS)
    public void checkOrdersStatus() {
        val createdOrders = tradeOrderRepository.getNewOrders();

        createdOrders.stream().parallel()
                .forEach(dbOrder -> {
                    var user = dbOrder.tinkoffUser();
                    var api = InvestApi.create(user.token());
                    var relevantOrder = api.getOrdersService().getOrderStateSync(user.accountId(), dbOrder.id());
                    var relevantStatus = OrderStatus.getByExecutionReportStatus(relevantOrder.getExecutionReportStatus());
                    if (relevantStatus != dbOrder.status()) {
                        // сохраняем статус в БД
                        dbOrder.status(relevantStatus);
                        tradeOrderRepository.save(dbOrder);

                        // отправляем уведомление
                        notificationClient.notify(user, relevantStatus, relevantOrder);
                    }
                });
    }
}