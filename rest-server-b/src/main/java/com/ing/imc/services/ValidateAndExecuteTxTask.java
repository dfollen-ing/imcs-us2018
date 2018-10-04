package com.ing.imc.services;

import com.ing.imc.domain.*;
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

import static com.ing.imc.Config.*;

@Slf4j
public class ValidateAndExecuteTxTask implements IgniteCallable<ErrorOrResult<Transaction>> {

    @IgniteInstanceResource
    private Ignite ignite;

    private final NewTransactionDto dto;

    ValidateAndExecuteTxTask(NewTransactionDto dto) {
        this.dto = dto;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public ErrorOrResult<Transaction> call() {

        try (org.apache.ignite.transactions.Transaction igniteTx = ignite.transactions().txStart()) {
            IgniteCache<AffinityKey<String>, Transaction> txCache = ignite.cache(TRANSACTION_CACHE_NAME);
            IgniteCache<AffinityKey<String>, Account> accountCache = ignite.cache(ACCOUNT_CACHE_NAME);
            IgniteCache<AffinityKey<String>, BinaryObject> accountCacheBinary = ignite.cache(ACCOUNT_CACHE_NAME).withKeepBinary();
            IgniteCache<String, BinaryObject> customerCacheBinary = ignite.cache(CUSTOMER_CACHE_NAME).withKeepBinary();

            BinaryObject binaryAccount = accountCacheBinary.get(new AffinityKey<>(dto.getAccountId(), dto.getOwnerId()));
            log.info("Got the following binary account:{}", binaryAccount);
            if (binaryAccount == null) {
                return new ErrorOrResult<>("Account " + dto.getAccountId() + " not found", 400);
            }
            // else
            Account account = Account.fromBinary(binaryAccount);
            TransactionType txType;
            if (account.getAccountNumber().equals(dto.getCreditAccountNumber())) {
                txType = TransactionType.CREDIT;
            } else if (account.getAccountNumber().equals(dto.getDebitAccountNumber())) {
                txType = TransactionType.DEBIT;
            } else {
                return new ErrorOrResult<>("Invalid transaction, not debit nor credit", 400);
            }

            // verify that currencies match
            if (!account.getCurrency().equals(dto.getCurrency())) {
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

                if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    igniteTx.rollback();
                    return new ErrorOrResult<>("Insufficient funds", 403);
                }
                // else, check if there is a limit
                BigDecimal limit = account.getLimit();

                log.info("Checking the limit if any");
                if ((limit != null) && (limit.compareTo(newBalance) > 0)) {
                    createAlert(customerCacheBinary, newTx, newBalance, limit);
                }

            } else { // CREDIT
                newBalance = account.getBalance().add(dto.getAmount());
            }
            // update the account
            account.setBalance(newBalance);

            log.info("Going to save the following account : {}", account);
//            accountCache.replace(account.getAffinityKey(), account); // => class not found
            accountCache.remove(account.getAffinityKey());
            accountCache.put(account.getAffinityKey(), account);

            // insert the new Tx
            txCache.put(newTx.getAffinityKey(), newTx);

            // we're OK now, all data is in there
            igniteTx.commit();
            return new ErrorOrResult<>(newTx);
        }
    }

    private void createAlert(IgniteCache<String, BinaryObject> customerCacheBinary, Transaction newTx, BigDecimal newBalance, BigDecimal limit) {
        log.info("We are under the limit");
        // we need to post an alert
        BinaryObject customerBinary = customerCacheBinary.get(dto.getOwnerId());
        Customer customer = Customer.fromBinary(customerBinary);
        IgniteCache<String, Alert> alertCache = ignite.cache(ALERT_CACHE_NAME);
        Alert alert = new Alert(UUID.randomUUID().toString(),
                customer.getContactDetails(),
                String.format("Careful, after a debit of %.2f %s, you will only have %.2f %s left, which is lower than the limit set up (%.2f %s)",
                        newTx.getAmount(),
                        newTx.getCurrency(),
                        newBalance,
                        newTx.getCurrency(),
                        limit,
                        newTx.getCurrency()),
                LocalDateTime.now());
        log.info("Going to post the following alert:{}", alert);
        alertCache.put(alert.getId(), alert);
    }
}
