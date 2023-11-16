package com.itmo.tinkoffinvestementbot.service.order;

import com.itmo.tinkoffinvestementbot.repository.TinkoffUserRepository;
import com.itmo.tinkoffinvestementbot.repository.TradeOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.PostOrderResponse;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoffinvestementbot.dto.OrderDto;
import tinkoffinvestementbot.model.OrderResult;
import tinkoffinvestementbot.model.OrderSide;

import static tinkoffinvestementbot.model.OrderStatus.WONT_EXECUTE;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractOrderServiceImpl implements OrderService {

    protected final PostOrderConverter postOrderConverter;
    protected final TinkoffUserRepository tinkoffUserRepository;
    protected final TradeOrderRepository tradeOrderRepository;

    public abstract InvestApi getInvestApi(String token);

    public abstract PostOrderResponse postOrder(InvestApi investApi, String instrumentId, Long quantity, OrderDirection orderDirection, String accountId);

    public OrderResult sendOrder(OrderDto orderDto) {
        val user = tinkoffUserRepository.get(orderDto.userId());
        val investApi = getInvestApi(user.token());

        val orderSide = orderDto.side();
        if (orderSide == OrderSide.HOLD) {
            log.warn("Не можем подать поручение с прогнозом Держать");
            return new OrderResult(null, WONT_EXECUTE, 0L, 0., 0.);
        }

        // TODO: по-хорошему, также надо проверить баланс/лимиты на счете

        val postOrderResponse = postOrder(investApi, orderDto.instrumentId(), orderDto.quantity(),
                orderSide.getDirection(), user.accountId());
        val result = postOrderConverter.convert(postOrderResponse);
        log.info("Saved order: {}", tradeOrderRepository.save(result, user));
        return result;
    }

}
