package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class DotenvMissingKeyTest {

//    @Test
//    void irrelevantKey_returnsNull() {
//        DotenvPropertySource source = new DotenvPropertySource(DotenvConfig.load(Map.of(
//            "springdotenv.directory", "dotenv",
//            "springdotenv.filename", "smoke.env",
//            "springdotenv.prefix", "other."
//        )));
//
//        assertThat(source.getProperty("other.VALUE")).isNull();
//    }

    @Test
    void missingKey_returnsNull() {
        DotenvPropertySource source = new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env"
        )));

        assertThat(source.getProperty("MISSING")).isNull();
    }
}
