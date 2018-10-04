package com.ing.imc.services;

import com.ing.imc.domain.Transaction;
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

import static com.ing.imc.Config.TRANSACTION_CACHE_NAME;

public class GetTransactionsTask implements IgniteCallable<List<Transaction>> {

    @IgniteInstanceResource
    private Ignite ignite;

    private final String accountId;


    public GetTransactionsTask(String accountId) {
        this.accountId = accountId;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<Transaction> call() {
        IgniteCache<String, BinaryObject> txCacheBinary = ignite.cache(TRANSACTION_CACHE_NAME).withKeepBinary();
        SqlQuery<BinaryObject, BinaryObject> sql = new SqlQuery<>(Transaction.class,"from Transaction where accountId = ?");
        sql.setArgs(accountId);

        ArrayList<Transaction> result = new ArrayList<>();
        try (QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> accounts = txCacheBinary.query(sql)) {
            accounts.forEach(entry -> result.add(Transaction.fromBinary(entry.getValue())));
        }

        return result;
    }


}
