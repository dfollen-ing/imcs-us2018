package com.ing.imc.services;

import com.ing.imc.domain.Account;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.List;

import static com.ing.imc.Config.ACCOUNT_CACHE_NAME;

public class GetAccountsTask implements IgniteCallable<List<Account>> {

    @IgniteInstanceResource
    private Ignite ignite;

    private final String customerId;


    GetAccountsTask(String customerId) {
        this.customerId = customerId;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<Account> call() {
        IgniteCache<String, BinaryObject> accountCacheBinary = ignite.cache(ACCOUNT_CACHE_NAME).withKeepBinary();
        SqlQuery<BinaryObject, BinaryObject> sql = new SqlQuery<>(Account.class, " from Account where ownerId = ?");
        sql.setArgs(customerId);

        ArrayList<Account> result = new ArrayList<>();
        try (QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> accounts = accountCacheBinary.query(sql)) {
            accounts.forEach(entry -> result.add(Account.fromBinary(entry.getValue())));
        }

        return result;
    }


}
