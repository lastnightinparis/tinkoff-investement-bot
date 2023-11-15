package com.itmo.tinkoffinvestementbot.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoffinvestementbot.model.User;

import static java.util.Objects.isNull;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    default User get(Long id) {
        var user = this.getReferenceById(id);
        if (isNull(user.accountId())) {
            var api = InvestApi.createSandbox(user.token());
            var sandboxAccountId = api.getSandboxService().getAccountsSync().stream().findFirst();
            var accountId = sandboxAccountId.map(Account::getId).orElseGet(() -> api.getSandboxService().openAccountSync());
            user.accountId(accountId);
            this.save(user);
        }
        return user;
    }

}
