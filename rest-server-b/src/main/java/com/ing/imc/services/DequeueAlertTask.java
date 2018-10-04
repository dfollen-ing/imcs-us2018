package com.ing.imc.services;

import com.ing.imc.domain.Alert;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;

import javax.cache.Cache;
import java.util.List;

import static com.ing.imc.Config.ALERT_CACHE_NAME;
import static com.ing.imc.domain.Alert.EMPTY_ALERT;

@Slf4j
public class DequeueAlertTask implements IgniteCallable<ErrorOrResult<Alert>> {

    @IgniteInstanceResource
    private Ignite ignite;

    @Override
    public ErrorOrResult<Alert> call() {
        IgniteCache<String, BinaryObject> alertCacheBinary = ignite.cache(ALERT_CACHE_NAME).withKeepBinary();
        SqlQuery<BinaryObject, BinaryObject> sql = new SqlQuery<>(Alert.class, "from Alert limit 1");

        try (QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> alerts = alertCacheBinary.query(sql)) {
            List<Cache.Entry<BinaryObject, BinaryObject>> all = alerts.getAll();
            log.info("Executed the query and got {}", all);
            if (! all.isEmpty()) {
                log.info("Processing an entry");
                Alert alert = Alert.fromBinary(all.get(0).getValue());
                alertCacheBinary.remove(alert.getId());
                return new ErrorOrResult<>(alert);
            }
        }
        // else if we get there, then there was no entry
        return new ErrorOrResult<>(EMPTY_ALERT);
    }
}
