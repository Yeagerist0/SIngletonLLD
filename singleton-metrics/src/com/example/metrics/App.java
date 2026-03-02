package com.example.metrics;

import java.util.Map;

/**
 * PulseMeter CLI entry point.
 *
 * After the singleton fix, the loaded and global instances are the same object.
 */
public class App {

    public static void main(String[] args) throws Exception {
        String propsPath = "metrics.properties";

        MetricsLoader loader = new MetricsLoader();
        MetricsRegistry loaded = loader.loadFromFile(propsPath);

        // In a correct design, loader should populate the SAME singleton instance.
        MetricsRegistry global = MetricsRegistry.getInstance();

        System.out.println("Loaded registry instance  : " + System.identityHashCode(loaded));
        System.out.println("Global registry instance  : " + System.identityHashCode(global));

        global.increment("REQUESTS_TOTAL");
        System.out.println("\nREQUESTS_TOTAL = " + global.getCount("REQUESTS_TOTAL"));

        System.out.println("\nAll counters:");
        for (Map.Entry<String, Long> e : global.getAll().entrySet()) {
            System.out.println("  " + e.getKey() + " = " + e.getValue());
        }

        System.out.println("\nTIP: Run ConcurrencyCheck / ReflectionAttack / SerializationCheck for validations.");
    }
}
