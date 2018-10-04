package com.ing.learn.imc.client.a_initial;

import com.ing.learn.imc.client.util.AbstractClientNode;
import org.apache.ignite.Ignite;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.configuration.CacheConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import static com.ing.imc.Config.*;
import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

public class CreateCaches extends AbstractClientNode {

    public static void main(String... args) {
        new CreateCaches().start();
    }

    @Override
    public void run(Ignite client) {
        client.cluster().active(true);
        createCustomerCache(client);
        createAccountCache(client);
        createTransactionCache(client);
    }

    private void createCustomerCache(Ignite client) {
        CacheConfiguration<String, Object> cacheConfiguration = new CacheConfiguration<>();
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
        cacheConfiguration.setBackups(1);
        cacheConfiguration.setName(CUSTOMER_CACHE_NAME);
        client.getOrCreateCache(cacheConfiguration);
    }

    private void createAccountCache(Ignite ignite) {
        CacheConfiguration<AffinityKey<String>, Object> cacheConfiguration = new CacheConfiguration<>();
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
        cacheConfiguration.setBackups(1);
        cacheConfiguration.setName(ACCOUNT_CACHE_NAME);
        cacheConfiguration.setQueryEntities(getQueryEntities());
        cacheConfiguration.setAtomicityMode(TRANSACTIONAL);
        ignite.getOrCreateCache(cacheConfiguration);
    }

    private Collection<QueryEntity> getQueryEntities() {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        fields.put("ownerId", String.class.getName());
        fields.put("balance", BigDecimal.class.getName());
        fields.put("id", String.class.getName());
        fields.put("accountNumber", String.class.getName());
        fields.put("currency", String.class.getName());
        fields.put("type", "com.ing.imc.domain.AccountType");
        QueryEntity queryEntity = new QueryEntity();
        queryEntity.setFields(fields);
        queryEntity.setValueType("com.ing.imc.domain.Account");
        queryEntity.setKeyType(AffinityKey.class.getName());
        queryEntity.setIndexes(Arrays.asList(new QueryIndex("ownerId"), new QueryIndex("accountNumber")));
        return Collections.singletonList(queryEntity);
    }

    private void createTransactionCache(Ignite ignite) {
        CacheConfiguration<AffinityKey<String>, Object> cacheConfiguration = new CacheConfiguration<>();
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
        cacheConfiguration.setBackups(1);
        cacheConfiguration.setName(TRANSACTION_CACHE_NAME);
        cacheConfiguration.setQueryEntities(getTxQueryEntities());
        ignite.getOrCreateCache(cacheConfiguration);
    }

    private Collection<QueryEntity> getTxQueryEntities() {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        fields.put("id", String.class.getName());
        fields.put("accountId", String.class.getName());
        fields.put("debitAccountNumber", String.class.getName());
        fields.put("creditAccountNumber", String.class.getName());
        fields.put("currency", String.class.getName());
        fields.put("amount", BigDecimal.class.getName());
        fields.put("communication", String.class.getName());
        fields.put("receivedTime", LocalDateTime.class.getName());
        fields.put("type", "com.ing.imc.domain.TransactionType");
        QueryEntity queryEntity = new QueryEntity();
        queryEntity.setFields(fields);
        queryEntity.setValueType("com.ing.imc.domain.Transaction");
        queryEntity.setKeyType(AffinityKey.class.getName());
        queryEntity.setIndexes(Collections.singletonList(new QueryIndex("accountId")));
        return Collections.singletonList(queryEntity);
    }

}
