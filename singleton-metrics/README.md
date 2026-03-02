# singleton-metrics

## Exercise A — Singleton Refactoring (Metrics Registry)

A CLI tool called **PulseMeter** collects runtime metrics (counters) and exposes them globally so any part of the app can increment counters like `REQUESTS_TOTAL`, `DB_ERRORS`, etc.

### What was fixed

1. **Thread-safe lazy initialization** — Used the **Bill Pugh static inner holder** idiom (`Holder` class). The JVM guarantees that the inner class is loaded only when `getInstance()` is first called, and class loading is inherently thread-safe.

2. **Reflection attack blocked** — Constructor is now `private` and checks a `volatile` flag `instanceCreated`. If someone tries to call the constructor via reflection after the singleton already exists, it throws a `RuntimeException`.

3. **Serialization safe** — Implemented `readResolve()` so that deserialization always returns the existing singleton instance instead of creating a new one.

4. **MetricsLoader fixed** — Replaced `new MetricsRegistry()` with `MetricsRegistry.getInstance()` so loaded metrics go into the same global singleton.

### Build & Run

```bash
cd singleton-metrics/src
javac com/example/metrics/*.java
java com.example.metrics.App
```

### Validation Checks

```bash
# Concurrency — must print "Unique instances seen: 1"
java com.example.metrics.ConcurrencyCheck

# Reflection — must print "Reflection attack blocked!"
java com.example.metrics.ReflectionAttack

# Serialization — must print "Same object? true"
java com.example.metrics.SerializationCheck
```
