package com.itmo.tinkoffinvestementbot.service.order;

import com.itmo.tinkoffinvestementbot.repository.TradeOrderRepository;
import com.itmo.tinkoffinvestementbot.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoffinvestementbot.model.OrderStatus;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStatusObserver {
    protected final UserRepository userRepository;
    protected final TradeOrderRepository tradeOrderRepository;

    @Transactional
    @Scheduled(fixedDelay = 30L, timeUnit = TimeUnit.SECONDS)
    public void checkOrdersStatus() {
        val createdOrders = tradeOrderRepository.getNewOrders();

        for (var dbOrder : createdOrders) {
            var user = dbOrder.user();
            var api = InvestApi.createSandbox(user.token());
            var relevantOrder = api.getSandboxService().getOrderStateSync(user.accountId(), dbOrder.id());
            var relevantStatus = OrderStatus.getByExecutionReportStatus(relevantOrder.getExecutionReportStatus());
            if (relevantStatus != dbOrder.status()) {
                // TODO: отправить уведомление об исполнении
                dbOrder.status(relevantStatus);
                tradeOrderRepository.save(dbOrder);
            }
        }
    }
}
