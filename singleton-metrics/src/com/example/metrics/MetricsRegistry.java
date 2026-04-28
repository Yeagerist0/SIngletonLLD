package com.example.metrics;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Global metrics registry — a proper, thread-safe, lazy-initialized Singleton.
 *
 * Fixes applied:
 *  1) Lazy initialization via static inner holder class (Bill Pugh idiom).
 *  2) Private constructor with reflection guard.
 *  3) readResolve() to preserve singleton on deserialization.
 */
public class MetricsRegistry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, Long> counters = new HashMap<>();

    // Static flag to detect reflection attacks
    private static volatile boolean instanceCreated = false;

    // Private constructor — blocks reflection-based second construction
    private MetricsRegistry() {
        if (instanceCreated) {
            throw new RuntimeException(
                "Singleton violation! Use MetricsRegistry.getInstance() instead.");
        }
        instanceCreated = true;
    }

    // Bill Pugh Singleton — lazy, thread-safe via class-loading guarantee
    private static class Holder {
        private static final MetricsRegistry INSTANCE = new MetricsRegistry();
    }

    public static MetricsRegistry getInstance() {
        return Holder.INSTANCE;
    }

    // Preserve singleton across serialization/deserialization
    @Serial
    private Object readResolve() {
        return getInstance();
    }

    public synchronized void setCount(String key, long value) {
        counters.put(key, value);
    }

    public synchronized void increment(String key) {
        counters.put(key, getCount(key) + 1);
    }

    public synchronized long getCount(String key) {
        return counters.getOrDefault(key, 0L);
    }

    public synchronized Map<String, Long> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }
}
