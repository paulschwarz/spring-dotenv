package me.paulschwarz.springdotenv.spring;

import java.util.HashMap;
import java.util.Map;
import me.paulschwarz.springdotenv.DotenvConfig;
import org.springframework.core.env.Environment;

public final class DotenvConfigExtractor {

    public static Map<String, String> from(Environment env) {
        Map<String, String> m = new HashMap<>();

        for (String key : DotenvConfig.ALL_KEYS) {
            String v = env.getProperty(key);
            if (v != null) {
                m.put(key, v);
            }
        }

        return m;
    }

    private DotenvConfigExtractor() {}
}
