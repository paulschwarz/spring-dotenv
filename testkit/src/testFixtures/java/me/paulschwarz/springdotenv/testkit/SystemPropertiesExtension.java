package me.paulschwarz.springdotenv.testkit;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SystemPropertiesExtension implements BeforeEachCallback, AfterEachCallback {

    private final Set<String> touchedKeys = new HashSet<>();
    private final Map<String, String> previous = new HashMap<>();

    @Override
    public void beforeEach(@NonNull ExtensionContext context) {
        touchedKeys.clear();
        previous.clear();
    }

    @Override
    public void afterEach(@NonNull ExtensionContext context) {
        for (String key : touchedKeys) {
            restore(key, previous.get(key));
        }
        touchedKeys.clear();
        previous.clear();
    }

    public void set(String key, String value) {
        snapshotIfFirstTouch(key);
        System.setProperty(key, value);
    }

    public void clear(String key) {
        snapshotIfFirstTouch(key);
        System.clearProperty(key);
    }

    private void snapshotIfFirstTouch(String key) {
        if (touchedKeys.add(key)) {
            // first time we touch this key in this test -> snapshot previous state
            previous.put(key, System.getProperty(key));
        }
    }

    private static void restore(String key, String prev) {
        if (prev == null) System.clearProperty(key);
        else System.setProperty(key, prev);
    }
}
