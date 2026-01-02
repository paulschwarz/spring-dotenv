package me.paulschwarz.springdotenv;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DotenvPropertyLoader {

    private static final Log log = LogFactory.getLog(DotenvPropertyLoader.class);

    private final Dotenv dotenv;

    /**
     * Maps canonicalKey -> original dotenv key. Example: "dotenvonly" -> "DOTENV_ONLY"
     */
    private final Map<String, String> relaxedKeyIndex;

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
        this.relaxedKeyIndex = buildRelaxedIndex(this.dotenv);
    }

    public Object getValue(String key) {
        // 1) exact match
        String exact = dotenv.get(key);
        if (exact != null) {
            return exact;
        }

        // 2) relaxed match
        String canonical = canonicalize(key);
        String actualKey = relaxedKeyIndex.get(canonical);
        return (actualKey != null) ? dotenv.get(actualKey) : null;
    }

    private static Map<String, String> buildRelaxedIndex(Dotenv dotenv) {
        Map<String, String> idx = new HashMap<>();

        dotenv.entries().forEach(entry -> {
            String originalKey = entry.getKey();
            String canonical = canonicalize(originalKey);

            String existing = idx.putIfAbsent(canonical, originalKey);
            if (existing != null && log.isDebugEnabled()) {
                log.debug(
                    "Dotenv relaxed-key collision for '" + canonical + "': "
                        + "keeping '" + existing + "', ignoring '" + originalKey + "'");
            }
        });

        return Map.copyOf(idx);
    }

    private static String canonicalize(String s) {
        String lower = s.toLowerCase(Locale.ROOT);
        StringBuilder out = new StringBuilder(lower.length());
        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if (c == '.' || c == '-' || c == '_') {
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }
}
