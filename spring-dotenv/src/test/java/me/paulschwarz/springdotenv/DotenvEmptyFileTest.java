package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import java.util.Map;
import org.junit.jupiter.api.Test;

class DotenvEmptyFileTest {

    @Test
    void emptyFile_returnsNull_forAnyKey() {
        DotenvPropertySource source = new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "empty.env"
        )));

        assertThat(source.getProperty("DOTENV_ONLY")).isNull();
        assertThat(source.getProperty("SOME_RANDOM_KEY")).isNull();
    }

    @Test
    void emptyFile_isNotMissing_evenWhenIgnoreIfMissingFalse() {
        assertThatNoException().isThrownBy(() -> new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "empty.env",
            "springdotenv.ignoreIfMissing", "false"
        ))));
    }
}
