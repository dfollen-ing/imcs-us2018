package com.ing.imc.services;

import com.ing.imc.domain.Account;
import com.ing.imc.domain.Customer;
import com.ing.imc.resources.LimitDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;

import java.math.BigDecimal;

import static com.ing.imc.Config.ACCOUNT_CACHE_NAME;
import static com.ing.imc.Config.CUSTOMER_CACHE_NAME;

@Slf4j
public class UpdateAmountLimitTask implements IgniteCallable<ErrorOrResult<BigDecimal>> {

    @IgniteInstanceResource
    private Ignite ignite;

    private final LimitDetailsDto dto;
    private final String accountId;

    UpdateAmountLimitTask(LimitDetailsDto dto, String accountId) {
        this.dto = dto;
        this.accountId = accountId;
    }

    @Override
    public ErrorOrResult<BigDecimal> call() {
        IgniteCache<String, Customer> customerCache = ignite.cache(CUSTOMER_CACHE_NAME);
        IgniteCache<String, BinaryObject> customerCacheBinary = ignite.cache(CUSTOMER_CACHE_NAME).withKeepBinary();
        IgniteCache<AffinityKey<String>, Account> accountCache = ignite.cache(ACCOUNT_CACHE_NAME);
        IgniteCache<AffinityKey<String>, BinaryObject> accountCacheBinary = ignite.cache(ACCOUNT_CACHE_NAME).withKeepBinary();

        BinaryObject customerBinary = customerCacheBinary.get(dto.getOwnerId());
        BinaryObject accountBinary = accountCacheBinary.get(new AffinityKey<>(accountId, dto.getOwnerId()));

        if (customerBinary == null) {
            return new ErrorOrResult<>("Customer "+dto.getOwnerId()+" not found", 404);
        }
        Customer customer = Customer.fromBinary(customerBinary);
        log.info("Got the following customer : {}", customer);

        if (accountBinary == null) {
            return new ErrorOrResult<>("Account "+accountId+" not found", 404);
        }
        Account account = Account.fromBinary(accountBinary);
        log.info("Got the following account : {}", account);

        // we just update the elements 1 by 1, no need for a TX here
        customer.setContactDetails(dto.getContactDetails());
        customerCache.replace(customer.getId(), customer);

        account.setLimit(new BigDecimal(dto.getLimitAmount()));
        log.info("Going to save the following account, with the new limit ({}) : {}", account.getLimit(), account);
        accountCache.replace(account.getAffinityKey(), account);

        return new ErrorOrResult<>(account.getLimit());
    }
}
