package com.itmo.tinkoffinvestementbot.service.order;

import com.itmo.tinkoffinvestementbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.PostOrderResponse;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoffinvestementbot.dto.OrderDto;
import tinkoffinvestementbot.model.OrderResult;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
abstract class AbstractOrderServiceImpl implements OrderService {

    protected final UserRepository userRepository;

    abstract InvestApi getInvestApi(String token);

    abstract PostOrderResponse postOrder(InvestApi investApi, String instrumentId, Long quantity, OrderDirection orderDirection, String accountId);

    public OrderResult sendOrder(OrderDto orderDto) {
        val user = userRepository.get(orderDto.userId());
        val investApi = getInvestApi(user.token());

        val orderDirection = orderDto.side().getDirection();
        if (orderDirection == OrderDirection.ORDER_DIRECTION_UNSPECIFIED) {
            log.warn("Не можем подать поручение с неизвестным типом");
            return new OrderResult(null, OrderResult.Status.WONT_EXECUTE, 0L, 0., 0.);
        }

        var rs = postOrder(investApi, orderDto.instrumentId(), orderDto.quantity(),
                orderDirection, user.accountId());
        return new OrderResult(rs.getOrderId(),
                OrderResult.Status.getByExecutionReportStatus(rs.getExecutionReportStatus()),
                rs.getLotsExecuted(),
                convertMoneyValue(rs.getExecutedCommission()),
                convertMoneyValue(rs.getExecutedOrderPrice()));
    }

    private double convertMoneyValue(MoneyValue value) {
        if (isNull(value)) {
            return 0.;
        }

        return BigDecimal.valueOf(value.getUnits())
                .add(BigDecimal.valueOf(value.getNano(), 9))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }


}
