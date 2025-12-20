package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class DotenvSmokeTest {

    @Test
    void dotenvOnly_resolvesFromDotenv() {
        DotenvPropertySource source = new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env"
        )));

        assertThat(source.getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
    }
}
