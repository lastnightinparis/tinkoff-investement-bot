package com.itmo.tinkoffinvestementbot.service.order;

import com.itmo.tinkoffinvestementbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.contract.v1.PostOrderResponse;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.core.InvestApi;

@Service
@Profile("!dev")
public class OrderServiceImpl extends AbstractOrderServiceImpl {

    @Autowired
    public OrderServiceImpl(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    InvestApi getInvestApi(String token) {
        return InvestApi.create(token);
    }

    @Override
    PostOrderResponse postOrder(InvestApi investApi, String instrumentId, Long quantity, OrderDirection orderDirection, String accountId) {
        return investApi.getOrdersService().postOrderSync(instrumentId,
                quantity,
                Quotation.getDefaultInstance(),
                orderDirection,
                accountId,
                OrderType.ORDER_TYPE_BESTPRICE,
                null);
    }


}
