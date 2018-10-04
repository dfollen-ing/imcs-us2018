package com.ing.learn.imc.client.util;

import org.apache.ignite.Ignite;

public class ClusterActivator extends AbstractClientNode {

    public static void main(String... args) {
        new ClusterActivator().start();
    }

    @Override
    public void run(Ignite client) {
        client.cluster().active(true);
    }
}
