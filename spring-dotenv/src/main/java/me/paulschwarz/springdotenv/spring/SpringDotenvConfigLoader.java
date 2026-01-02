package me.paulschwarz.springdotenv.spring;

import java.util.List;
import java.util.Locale;
import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.DotenvConfigLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

public class SpringDotenvConfigLoader implements DotenvConfigLoader {

    private static String dot(String name) {
        return SPRINGDOTENV + "." + name;
    }

    private static String env(String name) {
        return SPRINGDOTENV.toUpperCase(Locale.ROOT) + "_" + name;
    }

    @Override
    public DotenvConfig load(ConfigurableEnvironment env) {
        DotenvConfig defaults = DotenvConfig.defaults();

        boolean enabled = getBoolean(env, List.of(
            dot("enabled"),
            env("ENABLED")
        ), defaults.enabled());

        String directory = getString(env, List.of(
            dot("directory"),
            env("DIRECTORY")
        ), defaults.directory());

        String filename = getString(env, List.of(
            dot("filename"),
            env("FILENAME")
        ), defaults.filename());

        boolean ignoreIfMalformed = getBoolean(env, List.of(
            dot("ignoreIfMalformed"),
            env("IGNORE_IF_MALFORMED")
        ), defaults.ignoreIfMalformed());

        boolean ignoreIfMissing = getBoolean(env, List.of(
            dot("ignoreIfMissing"),
            env("IGNORE_IF_MISSING")
        ), defaults.ignoreIfMissing());

        boolean exportToSystemProperties = getBoolean(env, List.of(
            dot("exportToSystemProperties"),
            env("EXPORT_TO_SYSTEM_PROPERTIES"),
            dot("systemProperties"),           // legacy
            env("SYSTEM_PROPERTIES")           // legacy
        ), defaults.exportToSystemProperties());

        return new DotenvConfig(
            enabled,
            directory,
            filename,
            ignoreIfMalformed,
            ignoreIfMissing,
            exportToSystemProperties
        );
    }

    private static boolean getBoolean(Environment env, List<String> keys, boolean defaultValue) {
        String v = firstNonNull(env, keys);
        return (v == null) ? defaultValue : Boolean.parseBoolean(v.trim());
    }

    private static String getString(Environment env, List<String> keys, String defaultValue) {
        String v = firstNonNull(env, keys);
        return (v == null) ? defaultValue : v;
    }

    private static String firstNonNull(Environment env, List<String> keys) {
        for (String key : keys) {
            String value = env.getProperty(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
