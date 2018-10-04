package com.ing.imc.resources;

import com.ing.imc.services.AccountService;
import com.ing.imc.services.TransactionService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestingUtil {

    static final AccountService ACCOUNT_SERVICE = Mockito.mock(AccountService.class);
    static final TransactionService TRANSACTION_SERVICE = Mockito.mock(TransactionService.class);

    @Bean
    @Profile("unit")
    public AccountService accountService() {
        return ACCOUNT_SERVICE;
    }

    @Bean
    @Profile("unit")
    public TransactionService transactionService() {
        return TRANSACTION_SERVICE;
    }
}
