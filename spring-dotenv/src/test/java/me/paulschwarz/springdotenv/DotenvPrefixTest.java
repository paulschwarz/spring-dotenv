package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class DotenvPrefixTest {

    @Test
    void prefixEnabled_unprefixedKeyIsNull_prefixedKeyResolves() {
        DotenvPropertySource source = new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.prefix", "env."
        )));

        assertThat(source.getProperty("DOTENV_ONLY")).isNull();
        assertThat(source.getProperty("env.DOTENV_ONLY")).isEqualTo("from dotenv");
    }
}
