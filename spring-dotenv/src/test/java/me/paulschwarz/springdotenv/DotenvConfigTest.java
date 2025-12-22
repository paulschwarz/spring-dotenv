package me.paulschwarz.springdotenv;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DotenvConfigTest {

    @Test
    void verifyDotenvConfigOptions() {
        Assertions.assertThat(DotenvConfig.ALL_KEYS).hasSize(6);
    }

    @Test
    void defaults_are_used_when_nothing_is_set() {
        DotenvConfig cfg = DotenvConfig.load(Map.of());

        assertThat(cfg.enabled())
            .isEqualTo(DotenvConfig.DEFAULT_ENABLED);

        assertThat(cfg.directory())
            .isEqualTo(DotenvConfig.DEFAULT_DIRECTORY);

        assertThat(cfg.filename())
            .isEqualTo(DotenvConfig.DEFAULT_FILENAME);

        assertThat(cfg.ignoreIfMissing())
            .isEqualTo(DotenvConfig.DEFAULT_IGNORE_IF_MISSING);

        assertThat(cfg.ignoreIfMalformed())
            .isEqualTo(DotenvConfig.DEFAULT_IGNORE_IF_MALFORMED);

        assertThat(cfg.exportToSystemProperties())
            .isEqualTo(DotenvConfig.DEFAULT_EXPORT_TO_SYSTEM_PROPERTIES);
    }

    @Test
    void config_overrides_defaults() {
        DotenvConfig cfg = DotenvConfig.load(Map.of(
            "springdotenv.filename", ".env.local",
            "springdotenv.ignoreIfMissing", "false"
        ));

        assertThat(cfg.filename()).isEqualTo(".env.local");
        assertThat(cfg.ignoreIfMissing()).isFalse();
    }

    @Test
    void later_put_wins_when_merging_maps_sysprop_over_env() {
        // simulate production precedence: env first, then sysprops overwrite
        Map<String, String> merged = merge(
            Map.of(
                "springdotenv.filename", ".env.env",
                "springdotenv.ignoreIfMissing", "false"
            ),
            Map.of(
                "springdotenv.filename", ".env.sys",
                "springdotenv.ignoreIfMissing", "true"
            )
        );

        DotenvConfig cfg = DotenvConfig.load(merged);

        assertThat(cfg.filename()).isEqualTo(".env.sys");
        assertThat(cfg.ignoreIfMissing()).isTrue();
    }

    @Test
    void blank_values_are_treated_as_not_set() {
        DotenvConfig cfg = DotenvConfig.load(Map.of(
            "springdotenv.filename", "   "
        ));

        assertThat(cfg.filename()).isEqualTo(DotenvConfig.DEFAULT_FILENAME);
    }

    private static Map<String, String> merge(Map<String, String> lowPrecedence, Map<String, String> highPrecedence) {
        var m = new java.util.HashMap<>(lowPrecedence);
        m.putAll(highPrecedence);

        return m;
    }
}
