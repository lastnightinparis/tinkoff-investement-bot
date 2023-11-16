package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;
import tinkoffinvestementbot.model.UserToken;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final UserTokenRepository userTokenRepository;

    @Override
    public boolean checkToken(Long userId, String token) {
        final InvestApi sandbox = InvestApi.createSandbox(token);
        try {
            if (sandbox.getSandboxService().getAccountsSync().stream().findFirst().isEmpty()) {
                final String account = sandbox.getSandboxService().openAccountSync();
                userTokenRepository.save(new UserToken(userId, account));
            }
            return true;
        } catch (ApiRuntimeException e) {
            return false;
        }
    }

    @Override
    public void deleteToken(Long userId) {
        userTokenRepository.deleteById(userId);
    }
}
