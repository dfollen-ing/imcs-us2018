package com.ing.imc.services;

import com.ing.imc.domain.Alert;
import org.apache.ignite.Ignite;
import org.springframework.stereotype.Component;

@Component
public class AlertService {

    private final Ignite ignite;
    public AlertService(Ignite ignite) {
        this.ignite = ignite;
    }

    public ErrorOrResult<Alert> getLastAlert() {
        return ignite.compute(ignite.cluster()).call(new DequeueAlertTask());
    }
}
