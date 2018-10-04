package com.ing.imc;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    public static final String CUSTOMER_CACHE_NAME = "CUSTOMER_CACHE_NAME";
    public static final String ACCOUNT_CACHE_NAME = "ACCOUNT_CACHE_NAME";
    public static final String TRANSACTION_CACHE_NAME = "TRANSACTION_CACHE_NAME";

    @Bean
    Ignite ignite(IgniteConfiguration igniteConfiguration) {
        return Ignition.start(igniteConfiguration);
    }

    @Bean
    IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration configuration = new IgniteConfiguration();
        configuration.setClientMode(true);
        configuration.setPeerClassLoadingEnabled(true);
        configuration.setMetricsLogFrequency(0);
        return configuration;
    }

}
