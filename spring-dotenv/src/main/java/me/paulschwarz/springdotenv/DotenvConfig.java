package me.paulschwarz.springdotenv;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public record DotenvConfig(
    boolean enabled,
    String directory,
    String filename,
    boolean ignoreIfMalformed,
    boolean ignoreIfMissing,
    boolean exportToSystemProperties
) {

    private static final Log log = LogFactory.getLog(DotenvConfig.class);

    // Defaults
    public static final String DEFAULT_DIRECTORY = null;
    public static final String DEFAULT_FILENAME = null;
    public static final boolean DEFAULT_IGNORE_IF_MALFORMED = false;
    public static final boolean DEFAULT_IGNORE_IF_MISSING = true;
    public static final boolean DEFAULT_EXPORT_TO_SYSTEM_PROPERTIES = false;
    public static final boolean DEFAULT_ENABLED = true;

    // Keys
    private static final String PREFIX = "springdotenv.";
    private static final String K_ENABLED = PREFIX + "enabled";
    private static final String K_DIRECTORY = PREFIX + "directory";
    private static final String K_FILENAME = PREFIX + "filename";
    private static final String K_IGNORE_IF_MALFORMED = PREFIX + "ignoreIfMalformed";
    private static final String K_IGNORE_IF_MISSING = PREFIX + "ignoreIfMissing";
    private static final String K_EXPORT_TO_SYSTEM_PROPERTIES = PREFIX + "exportToSystemProperties";

    // Legacy (deprecated) key
    private static final String K_SYSTEM_PROPERTIES_LEGACY = PREFIX + "systemProperties";

    public static final List<String> ALL_KEYS = List.of(
        K_ENABLED,
        K_DIRECTORY,
        K_FILENAME,
        K_IGNORE_IF_MALFORMED,
        K_IGNORE_IF_MISSING,
        K_EXPORT_TO_SYSTEM_PROPERTIES,

        // Legacy (deprecated) key
        K_SYSTEM_PROPERTIES_LEGACY
    );

    /**
     * Production entry point
     */
    public static DotenvConfig load() {
        return load(buildEffectiveConfig());
    }

    /**
     * Test-friendly entry point
     */
    public static DotenvConfig load(Map<String, String> config) {
        // Presence triggers warning (even if overridden by the new key)
        boolean legacyPresent = config.containsKey(K_SYSTEM_PROPERTIES_LEGACY);
        boolean newPresent = config.containsKey(K_EXPORT_TO_SYSTEM_PROPERTIES);

        if (legacyPresent) {
            log.warn("Configuration key '" + K_SYSTEM_PROPERTIES_LEGACY
                + "' is deprecated and will be removed in a future release. "
                + "Please use '" + K_EXPORT_TO_SYSTEM_PROPERTIES + "' instead. "
                + "If both are set, '" + K_EXPORT_TO_SYSTEM_PROPERTIES + "' takes precedence.");
        }

        // Precedence: new wins, else legacy, else default
        boolean exportToSystemProperties = newPresent
            ? getBoolean(config, K_EXPORT_TO_SYSTEM_PROPERTIES, DEFAULT_EXPORT_TO_SYSTEM_PROPERTIES)
            : (legacyPresent
                ? getBoolean(config, K_SYSTEM_PROPERTIES_LEGACY, DEFAULT_EXPORT_TO_SYSTEM_PROPERTIES)
                : DEFAULT_EXPORT_TO_SYSTEM_PROPERTIES);

        return new DotenvConfig(
            getBoolean(config, K_ENABLED, DEFAULT_ENABLED),
            getString(config, K_DIRECTORY, DEFAULT_DIRECTORY),
            getString(config, K_FILENAME, DEFAULT_FILENAME),
            getBoolean(config, K_IGNORE_IF_MALFORMED, DEFAULT_IGNORE_IF_MALFORMED),
            getBoolean(config, K_IGNORE_IF_MISSING, DEFAULT_IGNORE_IF_MISSING),
            exportToSystemProperties
        );
    }

    // ---- Helpers ----

    private static String getString(
        Map<String, String> map,
        String key,
        String defaultValue
    ) {
        String v = map.get(key);
        if (v == null) {
            return defaultValue;
        }

        String t = v.trim();

        return t.isEmpty() ? defaultValue : t;
    }

    private static boolean getBoolean(
        Map<String, String> map,
        String key,
        boolean defaultValue
    ) {
        String v = map.get(key);

        return v == null ? defaultValue : Boolean.parseBoolean(v.trim());
    }

    // ---- Map construction (sysprop > env) ----

    static Map<String, String> buildEffectiveConfig() {
        Map<String, String> map = new HashMap<>();

        // 1. env vars (lower precedence)
        System.getenv().forEach((k, v) -> {
            if (k.startsWith("SPRINGDOTENV_")) {
                map.put(envToKey(k), v);
            }
        });

        // 2. system properties (higher precedence)
        Properties sp = System.getProperties();
        sp.forEach((k, v) -> {
            String key = k.toString();
            if (key.startsWith(PREFIX)) {
                map.put(key, v.toString());
            }
        });

        return map;
    }

    private static String envToKey(String env) {
        // SPRINGDOTENV_IGNORE_IF_MISSING -> springdotenv.ignoreIfMissing
        String body = env.substring("SPRINGDOTENV_".length()).toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder(PREFIX);

        boolean upperNext = false;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '_') {
                upperNext = true;
            } else if (upperNext) {
                sb.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
