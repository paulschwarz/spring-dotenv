package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.junit.jupiter.api.Test;

class DotenvIgnoreIfMissingTest {

    @Test
    void ignoreIfMissing_true_doesNotThrow_andReturnsNull() {
        DotenvPropertySource source = new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "does-not-exist.env",
            "springdotenv.ignoreIfMissing", "true"
        )));

        assertThatNoException().isThrownBy(() -> source.getProperty("DOTENV_ONLY"));
        assertThat(source.getProperty("DOTENV_ONLY")).isNull();
    }

    @Test
    void ignoreIfMissing_false_throws() {
        assertThatThrownBy(() -> new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "does-not-exist.env",
            "springdotenv.ignoreIfMissing", "false"
        ))))
            .hasMessageContaining("Could not find")
            .hasMessageContaining("dotenv/does-not-exist.env");
    }
}
