package com.ing.learn.imc.client.util;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;

public abstract class AbstractClientNode {


    public void start() {
        Ignite client = null;
        try {
            client = startClient();
            run(client);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    public abstract void run(Ignite client);

    private Ignite startClient() {
        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setClientMode(true);
        configuration.setGridLogger(new Slf4jLogger());
        configuration.setPeerClassLoadingEnabled(true);
        return Ignition.start(configuration);
    }
}
