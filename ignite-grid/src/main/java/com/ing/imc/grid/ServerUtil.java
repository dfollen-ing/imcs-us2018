package com.ing.imc.grid;

class ServerUtil {
    static void initSystemProperties() {
        String userDir = System.getProperty("user.dir");
        System.setProperty("IGNITE_HOME", userDir+"\\ignite-grid\\target\\ignite");
        System.setProperty("IGNITE_PERFORMANCE_SUGGESTIONS_DISABLED", "true");
    }
}
