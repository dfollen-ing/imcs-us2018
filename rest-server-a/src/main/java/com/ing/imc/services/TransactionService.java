package com.ing.imc.services;

import com.ing.imc.domain.Transaction;
import com.ing.imc.resources.NewTransactionDto;
import org.apache.ignite.Ignite;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.ing.imc.Config.TRANSACTION_CACHE_NAME;

@Component
public class TransactionService {

    private final Ignite ignite;

    public TransactionService(Ignite ignite) {
        this.ignite = ignite;
    }

    public List<Transaction> getTransactionsFor(String accountId) {
        return ignite.compute().affinityCall(Collections.singletonList(TRANSACTION_CACHE_NAME), accountId,
                new GetTransactionsTask(accountId));
    }

    public ErrorOrResult<Transaction> validateAndExecuteTx(NewTransactionDto transactionDto) {
        return ignite.compute().affinityCall(Collections.singletonList(TRANSACTION_CACHE_NAME), transactionDto.getAccountId(),
                new ValidateAndExecuteTxTask(transactionDto));
    }
}
