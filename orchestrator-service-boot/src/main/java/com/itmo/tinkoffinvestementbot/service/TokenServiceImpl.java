package com.itmo.tinkoffinvestementbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    @Override
    public boolean checkToken(String token) {
        final InvestApi sandbox = InvestApi.createSandbox(token);
        return sandbox.getSandboxService().getAccountsSync().stream().findFirst().isPresent();
    }
}
