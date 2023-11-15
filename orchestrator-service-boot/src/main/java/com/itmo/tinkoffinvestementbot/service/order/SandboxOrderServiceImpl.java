package com.itmo.tinkoffinvestementbot.service.order;

import com.itmo.tinkoffinvestementbot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;

import static java.util.UUID.randomUUID;

@Slf4j
@Service
@Profile("dev")
public class SandboxOrderServiceImpl extends AbstractOrderServiceImpl {

    @Autowired
    public SandboxOrderServiceImpl(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    InvestApi getInvestApi(String token) {
        return InvestApi.createSandbox(token);
    }

    @Override
    PostOrderResponse postOrder(InvestApi investApi, String instrumentId, Long quantity, OrderDirection orderDirection, String accountId) {
        if (investApi.getSandboxService().getPositionsSync(accountId).getMoneyCount() < 5_000) {
            investApi.getSandboxService().payIn(accountId, MoneyValue.newBuilder()
                    .setCurrency("RUB")
                    .setUnits(5_000L)
                    .setNano(10)
                    .build());
        }

        var orderId = randomUUID().toString();
        log.info("Формируем заявку в песочницу id={}", randomUUID());
        var rs = investApi.getSandboxService().postOrderSync(instrumentId,
                quantity,
                Quotation.getDefaultInstance(),
                orderDirection,
                accountId,
                OrderType.ORDER_TYPE_BESTPRICE,
                orderId);
        log.info("Получен ответ: {}", rs);
        return rs;
    }
}
