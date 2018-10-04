package com.ing.imc.grid;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;

import static org.apache.ignite.configuration.WALMode.BACKGROUND;

@Slf4j
class ServerNodePersistent {

    private static final String IGNITE_HOME = "IGNITE_HOME";

    void start(String id) {
        ServerUtil.initSystemProperties();
        log.debug("Ignite home is {}", System.getProperty(IGNITE_HOME));
        // start a server node
        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setGridLogger(new Slf4jLogger());
        configuration.setPeerClassLoadingEnabled(true);
        configuration.setMetricsLogFrequency(0L);
        configuration.setDataStorageConfiguration(getDataStorageConfiguration(id));
        Ignition.start(configuration);
    }

    private DataStorageConfiguration getDataStorageConfiguration(String id) {
        DataStorageConfiguration configuration = new DataStorageConfiguration();

        configuration.setStoragePath(System.getProperty(IGNITE_HOME)+"/"+id+"/store");
        configuration.setWalArchivePath(System.getProperty(IGNITE_HOME)+"/"+id+"/wal");
        configuration.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
        configuration.setWalMode(BACKGROUND);
        return configuration;
    }

}
