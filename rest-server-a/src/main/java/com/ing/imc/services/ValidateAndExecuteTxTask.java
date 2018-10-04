package com.ing.imc.services;

import com.ing.imc.domain.Account;
import com.ing.imc.domain.Transaction;
import com.ing.imc.domain.TransactionType;
import com.ing.imc.resources.NewTransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ing.imc.Config.ACCOUNT_CACHE_NAME;
import static com.ing.imc.Config.TRANSACTION_CACHE_NAME;

@Slf4j
public class ValidateAndExecuteTxTask implements IgniteCallable<ErrorOrResult<Transaction>> {

    @IgniteInstanceResource
    private Ignite ignite;

    private final NewTransactionDto dto;

    ValidateAndExecuteTxTask(NewTransactionDto dto) {
        this.dto = dto;
    }

    @Override
    public ErrorOrResult<Transaction> call() {
        try (org.apache.ignite.transactions.Transaction tx = ignite.transactions().txStart()) {
            IgniteCache<AffinityKey<String>, Transaction> txCache = ignite.cache(TRANSACTION_CACHE_NAME);
            IgniteCache<AffinityKey<String>, Account> accountCache = ignite.cache(ACCOUNT_CACHE_NAME);
            IgniteCache<AffinityKey<String>, BinaryObject> accountCacheBinary = accountCache.withKeepBinary();


            BinaryObject binaryAccount = accountCacheBinary.get(new AffinityKey<>(dto.getAccountId(), dto.getOwnerId()));
            if (binaryAccount == null) {
                tx.rollback();
                return new ErrorOrResult<>("Account "+ dto.getAccountId()+" not found", 400);
            }
            // else
            Account account = Account.fromBinary(binaryAccount);
            TransactionType txType;
            if (account.getAccountNumber().equals(dto.getCreditAccountNumber())) {
                txType = TransactionType.CREDIT;
            } else if (account.getAccountNumber().equals(dto.getDebitAccountNumber())) {
                txType = TransactionType.DEBIT;
            } else {
                tx.rollback();
                return new ErrorOrResult<>("Invalid transaction, not debit nor credit", 400);
            }

            // verify that currencies match
            if (!account.getCurrency().equals(dto.getCurrency())) {
                tx.rollback();
                return new ErrorOrResult<>("Currencies do not match", 400);
            }

            String txId = UUID.randomUUID().toString();
            Transaction newTx = new Transaction(txId,
                    dto.getAccountId(),
                    dto.getDebitAccountNumber(),
                    dto.getCreditAccountNumber(),
                    dto.getCurrency(),
                    dto.getAmount(),
                    dto.getCommunication(),
                    LocalDateTime.now(),
                    txType);

            BigDecimal newBalance;
            if (txType == TransactionType.DEBIT) {
                newBalance = account.getBalance().subtract(dto.getAmount());
            } else {
                newBalance = account.getBalance().add(dto.getAmount());
            }
            // check balance in transaction

            if (txType == TransactionType.DEBIT && newBalance.compareTo(BigDecimal.ZERO) < 0) {
                tx.rollback();
                return new ErrorOrResult<>("Insufficient funds", 403);
            }

            // update the account
            account.setBalance(newBalance);
            accountCache.remove(account.getAffinityKey());
            accountCache.put(account.getAffinityKey(), account);

            // insert the new Tx
            log.info("Will store {}", newTx);
            txCache.put(newTx.getAffinityKey(), newTx);

            // we're OK now, all data is in there
            tx.commit();
            return new ErrorOrResult<>(newTx);
        }
    }
}
