package com.ing.learn.imc.client.b_limits;

import com.ing.imc.domain.Customer;
import com.ing.learn.imc.client.util.AbstractClientNode;
import org.apache.ignite.Ignite;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

public class CreateAlertCache extends AbstractClientNode {

    public static void main(String... args) {
        new CreateAlertCache().start();
    }

    @Override
    public void run(Ignite client) {
        client.cluster().active(true);
        createAlertCache(client);
    }

    private void createAlertCache(Ignite ignite) {
        ignite.destroyCache("ALERT_CACHE_NAME");

        CacheConfiguration<String, Customer> cacheConfiguration = new CacheConfiguration<>();
        cacheConfiguration.setCacheMode(CacheMode.PARTITIONED);
        cacheConfiguration.setBackups(1);
        cacheConfiguration.setName("ALERT_CACHE_NAME");
        cacheConfiguration.setQueryEntities(getQueryEntities());
        ignite.getOrCreateCache(cacheConfiguration);
    }

    private Collection<QueryEntity> getQueryEntities() {
        LinkedHashMap<String, String> fields = new LinkedHashMap<>();
        fields.put("destination", String.class.getName());
        QueryEntity queryEntity = new QueryEntity();
        queryEntity.setFields(fields);
        queryEntity.setValueType("com.ing.imc.domain.Alert");
        queryEntity.setKeyType(String.class.getName());
        return Collections.singletonList(queryEntity);
    }

}
