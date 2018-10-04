package com.ing.imc.services;

import com.ing.imc.domain.Account;
import com.ing.imc.resources.LimitDetailsDto;
import org.apache.ignite.Ignite;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static com.ing.imc.Config.ACCOUNT_CACHE_NAME;

@Component
public class AccountService {

    private final Ignite ignite;
    public AccountService(Ignite ignite) {
        this.ignite = ignite;
    }

    public List<Account> getAccountsOf(String custId) {
        return ignite.compute().affinityCall(Collections.singletonList(ACCOUNT_CACHE_NAME), custId,
                new GetAccountsTask(custId));
    }

    public ErrorOrResult<BigDecimal> updateAmountLimit(LimitDetailsDto limitDetailsDto, String accountId) {
        return ignite.compute().affinityCall(Collections.singletonList(ACCOUNT_CACHE_NAME), accountId,
                new UpdateAmountLimitTask(limitDetailsDto, accountId));
    }
}
