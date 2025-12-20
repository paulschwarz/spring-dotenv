package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import org.junit.jupiter.api.Test;

class DotenvIgnoreIfMalformedTest {

    @Test
    void ignoreIfMalformed_true_skipsBadLines_andStillLoadsGoodOnes() {
        DotenvPropertySource source = new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "malformed.env",
            "springdotenv.ignoreIfMalformed", "true"
        )));

        assertThat(source.getProperty("GOOD")).isEqualTo("still good");
        assertThat(source.getProperty("ALSO_BAD")).isNull();
    }

    @Test
    void ignoreIfMalformed_false_throws_onMalformedDotenv() {
        assertThatThrownBy(() -> new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "malformed.env",
            "springdotenv.ignoreIfMalformed", "false"
        ))))
            .message()
            .containsIgnoringCase("malformed entry")
            .contains("THIS_IS_NOT_A_VALID_LINE");
    }
}
