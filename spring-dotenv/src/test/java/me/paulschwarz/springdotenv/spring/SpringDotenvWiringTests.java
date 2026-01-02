package me.paulschwarz.springdotenv.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.spring.DotenvEnvironmentApplier;
import me.paulschwarz.springdotenv.spring.SpringDotenvConfigLoader;
import me.paulschwarz.springdotenv.testkit.SystemPropertiesExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

@ResourceLock(Resources.SYSTEM_PROPERTIES)
class SpringDotenvWiringTests {

    @RegisterExtension
    static final SystemPropertiesExtension sys = new SystemPropertiesExtension();

    private final SpringDotenvConfigLoader loader = new SpringDotenvConfigLoader();

    @Test
    void shouldNotApplyDotenvPropertySource_whenDisabledInEnvironment() {
        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.enabled", "false"
        )));

        DotenvConfig config = loader.load(env);
        DotenvEnvironmentApplier.apply(env, config);

        assertThat(env.getPropertySources().contains("dotenvPropertySource")).isFalse();
    }

    @Test
    void shouldApplyDotenvPropertySource_whenEnabledInEnvironment() {
        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.enabled", "true"
        )));

        DotenvConfig config = loader.load(env);
        DotenvEnvironmentApplier.apply(env, config);

        assertThat(config.enabled()).isTrue();
        assertThat(env.getPropertySources().contains("dotenvPropertySource")).isTrue();
    }

    @Test
    void shouldLetApplicationPropertiesOverrideDotenv_whenBothArePresent() {
        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("app", Map.of(
            "DOTENV_ONLY", "from app",
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.enabled", "true"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvEnvironmentApplier.apply(env, cfg);

        assertThat(env.getProperty("DOTENV_ONLY")).isEqualTo("from app");
    }

    @Test
    void shouldResolveFromDotenv_whenApplicationPropertiesDoNotContainKey() {
        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("app", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.enabled", "true"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvEnvironmentApplier.apply(env, cfg);

        assertThat(env.getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
    }

    @Test
    void shouldLetSystemPropertiesOverrideDotenv_whenBothArePresent() {
        sys.set("DOTENV_ONLY", "from sysprops");

        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("app", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.enabled", "true"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvEnvironmentApplier.apply(env, cfg);

        assertThat(env.getProperty("DOTENV_ONLY")).isEqualTo("from sysprops");
    }
}
