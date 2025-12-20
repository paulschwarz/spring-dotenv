package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import me.paulschwarz.springdotenv.spring.DotenvEnvironmentApplier;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

public class DotenvEnabledSwitchTest {

    @Test
    void dotenv_is_not_applied_when_disabled_in_environment() {
        ConfigurableEnvironment env = new StandardEnvironment();
        DotenvConfig config = DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.enabled", "false"
        ));

        DotenvEnvironmentApplier.apply(env, config);

        assertThat(env.getPropertySources().contains("dotenvPropertySource")).isFalse();
        assertThat(env.getProperty("DOTENV_ONLY")).isNull();
    }

    @Test
    void dotenv_is_applied_when_enabled_in_environment() {
        ConfigurableEnvironment env = new StandardEnvironment();
        DotenvConfig config = DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.enabled", "true"
        ));

        DotenvEnvironmentApplier.apply(env, config);

        assertThat(env.getPropertySources().contains("dotenvPropertySource")).isTrue();
        assertThat(env.getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
    }
}
