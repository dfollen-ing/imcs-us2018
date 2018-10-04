package com.ing.learn.imc.client.a_initial;

import com.ing.imc.domain.Account;
import com.ing.imc.domain.AccountType;
import com.ing.imc.domain.Customer;
import com.ing.learn.imc.client.util.AbstractClientNode;
import com.ing.learn.imc.client.util.AccountUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.affinity.AffinityKey;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

import static com.ing.imc.Config.ACCOUNT_CACHE_NAME;
import static com.ing.imc.Config.CUSTOMER_CACHE_NAME;
import static com.ing.imc.domain.AccountType.*;
import static com.ing.learn.imc.client.util.CustomerUtil.generateRandomCustomer;

public class PopulateCustomerAndAccountData extends AbstractClientNode {

    private static final int NB_CUSTOMERS = 1_000;

    public static void main(String... args) {
        new PopulateCustomerAndAccountData().start();
    }

    @Override
    public void run(Ignite client) {
        client.cluster().active(true);
        IgniteCache<String, Customer> customerCache = client.cache(CUSTOMER_CACHE_NAME);
        IgniteCache<AffinityKey<String>, Account> accountCache = client.cache(ACCOUNT_CACHE_NAME);

        populateAccountCache(customerCache, accountCache);
    }

    private void populateAccountCache(IgniteCache<String, Customer> customerCache, IgniteCache<AffinityKey<String>, Account> accountCache) {
        // basic customer that does not change
        customerCache.put("0", generateRandomCustomer("0"));
        Account current = new Account("account_1", "BE12 3456 6789 0000", "EUR", new BigDecimal(4500), "0", CURRENT);
        Account savings = new Account("account_2", "BE45 3456 6789 0000", "EUR", new BigDecimal(10_000), "0", SAVINGS);
        Account trading = new Account("account_3", "BE67 3456 6789 0000", "EUR", new BigDecimal(7_000), "0", TRADING);
        accountCache.put(current.getAffinityKey(), current);
        accountCache.put(savings.getAffinityKey(), savings);
        accountCache.put(trading.getAffinityKey(), trading);

        for (int i = 1; i < NB_CUSTOMERS; i++) {
            String custId = Integer.toString(i);
            customerCache.put(custId, generateRandomCustomer(custId));
            accountCache.putAll(createAccounts(custId));
        }
    }

    private HashMap<AffinityKey<String>, Account> createAccounts(String custId) {
        HashMap<AffinityKey<String>, Account> accounts = new HashMap<>();
        accounts.clear();
        Account account = AccountUtil.generateAccount(UUID.randomUUID().toString(), custId, AccountType.CURRENT);
        accounts.put(account.getAffinityKey(), account);
        account = AccountUtil.generateAccount(UUID.randomUUID().toString(), custId, AccountType.SAVINGS);
        accounts.put(account.getAffinityKey(), account);
        account = AccountUtil.generateAccount(UUID.randomUUID().toString(), custId, AccountType.TRADING);
        accounts.put(account.getAffinityKey(), account);

        return accounts;
    }

}
