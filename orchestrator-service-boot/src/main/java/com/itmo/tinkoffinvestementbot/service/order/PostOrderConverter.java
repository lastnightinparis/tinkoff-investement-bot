package com.itmo.tinkoffinvestementbot.service.order;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.PostOrderResponse;
import tinkoffinvestementbot.model.OrderResult;
import tinkoffinvestementbot.model.OrderStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Objects.isNull;

@Component
public class PostOrderConverter implements Converter<PostOrderResponse, OrderResult> {
    @Override
    @NonNull
    public OrderResult convert(PostOrderResponse postOrderResponse) {
        return new OrderResult(postOrderResponse.getOrderId(),
                OrderStatus.getByExecutionReportStatus(postOrderResponse.getExecutionReportStatus()),
                postOrderResponse.getLotsExecuted(),
                convertMoneyValue(postOrderResponse.getExecutedCommission()),
                convertMoneyValue(postOrderResponse.getExecutedOrderPrice()));
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
