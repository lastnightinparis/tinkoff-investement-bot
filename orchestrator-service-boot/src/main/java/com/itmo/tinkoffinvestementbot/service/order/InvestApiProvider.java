package com.itmo.tinkoffinvestementbot.service.order;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoffinvestementbot.model.TinkoffUser;

@Service
public class InvestApiProvider {

    @Cacheable(value = "apis", key = "#tinkoffUser.token")
    public InvestApi getInvestApi(TinkoffUser tinkoffUser) {
        return InvestApi.createSandbox(tinkoffUser.token());
    }

}
