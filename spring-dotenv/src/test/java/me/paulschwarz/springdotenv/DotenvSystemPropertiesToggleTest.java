package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import me.paulschwarz.springdotenv.testkit.SystemPropertiesExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

@ResourceLock(Resources.SYSTEM_PROPERTIES)
class DotenvSystemPropertiesToggleTest {

    @RegisterExtension
    static final SystemPropertiesExtension sys = new SystemPropertiesExtension();

    @BeforeEach
    void setUp() {
        sys.clear("DOTENV_ONLY");
    }

    @Test
    void does_export_when_enabled() {
        new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.systemProperties", "true"
        )));

        assertThat(System.getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
    }

    @Test
    void does_not_export_when_disabled() {
        new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.systemProperties", "false"
        )));

        assertThat(System.getProperty("DOTENV_ONLY")).isNull();
    }
}
