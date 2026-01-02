package me.paulschwarz.springdotenv;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class DotenvPropertyLoader {

    private final Dotenv dotenv;

    public DotenvPropertyLoader(DotenvConfig dotenvConfig) {
        DotenvBuilder dotenvBuilder = Dotenv.configure();

        Optional.ofNullable(dotenvConfig.directory()).ifPresent(dotenvBuilder::directory);
        Optional.ofNullable(dotenvConfig.filename()).ifPresent(dotenvBuilder::filename);

        if (dotenvConfig.ignoreIfMalformed()) {
            dotenvBuilder.ignoreIfMalformed();
        }

        if (dotenvConfig.ignoreIfMissing()) {
            dotenvBuilder.ignoreIfMissing();
        }

        if (dotenvConfig.exportToSystemProperties()) {
            dotenvBuilder.systemProperties();
        }

        this.dotenv = dotenvBuilder.load();
    }

    /**
     * Strict lookup (plain Spring behavior).
     */
    public Object getValue(String key) {
        return dotenv.get(key);
    }

    /**
     * Raw dotenv entries as a Map, keys unchanged (ENV_VAR style).
     */
    public Map<String, Object> asMap() {
        Map<String, Object> out = new LinkedHashMap<>();
        dotenv.entries().forEach(e -> out.put(e.getKey(), e.getValue()));
        return Map.copyOf(out);
    }
}
