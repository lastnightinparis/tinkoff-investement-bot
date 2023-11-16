package com.itmo.tinkoffinvestementbot.service;

import com.itmo.tinkoffinvestementbot.repository.TinkoffUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.AccountType;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;
import tinkoffinvestementbot.model.TinkoffUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TinkoffUserRepository userTokenRepository;

    @Override
    public boolean checkToken(Long userId, String token) {
        val sandbox = InvestApi.createSandbox(token);
        val accounts = sandbox.getSandboxService().getAccountsSync();
        if (accounts.size() != 1) {
            log.warn("Токен выпущен для нескольких счетов");
            return false;
        }

        try {
            val optionalAccount = accounts.stream()
                    .filter(account -> account.hasOpenedDate() // счет открыт
                            && !account.hasClosedDate() // счет не закрыт
                            && account.getType() == AccountType.ACCOUNT_TYPE_TINKOFF // это обычный брокерский счет
                    )
                    .findFirst();

            if (optionalAccount.isEmpty()) {
                return false;
            }

            userTokenRepository.save(new TinkoffUser(userId, optionalAccount.get().getId(), token));
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
