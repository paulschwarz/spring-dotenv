package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.springboot.testkit.Boot4TestAppRunner.run;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import me.paulschwarz.springdotenv.testkit.EmptyConfig;
import me.paulschwarz.springdotenv.testkit.SystemPropertiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import org.springframework.context.ConfigurableApplicationContext;

class Boot4DotenvBehaviorTests {

    @RegisterExtension
    static final SystemPropertiesExtension sys = new SystemPropertiesExtension();

    @Test
    void shouldLoadDotenv_byDefault() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=smoke.env"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
        }
    }

    @Test
    void shouldSupportRelaxedBindingOfEnv() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=smoke.env"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
            assertThat(ctx.getEnvironment().getProperty("dotenv.only")).isEqualTo("from dotenv");
            assertThat(ctx.getEnvironment().getProperty("dotenv-only")).isEqualTo("from dotenv");
            assertThat(ctx.getEnvironment().getProperty("dotenv_only")).isEqualTo("from dotenv");
            assertThat(ctx.getEnvironment().getProperty("my.service[0].other")).isEqualTo("zero");
            assertThat(ctx.getEnvironment().getProperty("my.service[1].other")).isEqualTo("one");
            assertThat(ctx.getEnvironment().getProperty("my.list[0]")).isEqualTo("zero");
            assertThat(ctx.getEnvironment().getProperty("my.list[1]")).isEqualTo("one");
            assertThat(ctx.getEnvironment().getProperty("my-list-1")).isEqualTo("one");
        }
    }

    @Test
    void shouldLoadDotenv_whenEnabledIsTrue() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=smoke.env",
            "--springdotenv.enabled=true"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
        }
    }

    @Test
    void shouldNotLoadDotenv_whenEnabledIsFalse() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=smoke.env",
            "--springdotenv.enabled=false"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY")).isNull();
        }
    }

    @Test
    void shouldStart_whenDotenvIsMissing_andIgnoreIfMissingIsTrue() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=does-not-exist.env",
            "--springdotenv.ignoreIfMissing=true"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY")).isNull();
        }
    }

    @Test
    void shouldFailToStart_whenDotenvIsMissing_andIgnoreIfMissingIsFalse() {
        assertThatThrownBy(() -> run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=does-not-exist.env",
            "--springdotenv.ignoreIfMissing=false"
        ))
            .hasMessageContaining("Could not find")
            .hasMessageContaining("dotenv/does-not-exist.env");
    }

    @Test
    void shouldNotOverrideHigherPrecedenceEnvironmentVariables_whenDotenvIsEmpty() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=empty.env"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_AND_ENV")).isEqualTo("from env");
        }
    }

    @Test
    void shouldLetEnvironmentVariablesOverrideDotenv_whenBothArePresent() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=precedence.env"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
            assertThat(ctx.getEnvironment().getProperty("DOTENV_AND_ENV")).isEqualTo("from env");
        }
    }

    @Test
    void shouldLetCommandLineArgumentsOverrideDotenv_whenBothArePresent() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=precedence.env",
            "--DOTENV_AND_CLI=from cli"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_AND_CLI")).isEqualTo("from cli");
        }
    }

    @Test
    void shouldStart_whenDotenvIsMalformed_andIgnoreIfMalformedIsTrue() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=malformed.env",
            "--springdotenv.ignoreIfMalformed=true"
        )) {
            assertThat(ctx.getEnvironment().getProperty("GOOD")).isEqualTo("still good");
        }
    }

    @Test
    void shouldFailToStart_whenDotenvIsMalformed_andIgnoreIfMalformedIsFalse() {
        assertThatThrownBy(() -> run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=malformed.env",
            "--springdotenv.ignoreIfMalformed=false"
        ))
            .hasMessageContaining("Malformed entry");
    }

    @Test
    void shouldLetDotenvOverrideApplicationProperties_whenBothArePresent() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=smoke.env"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
        }
    }

    @Test
    @ResourceLock(Resources.SYSTEM_PROPERTIES)
    void dotenvIsLoadedWhenExportToSystemProperties_true() {
        sys.clear("DOTENV_ONLY");

        try (ConfigurableApplicationContext ignored = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=smoke.env",
            "--springdotenv.exportToSystemProperties=true"
        )) {
            assertThat(System.getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
        }
    }

    @Test
    @ResourceLock(Resources.SYSTEM_PROPERTIES)
    void dotenvIsLoadedWhenExportToSystemProperties_false() {
        sys.clear("DOTENV_ONLY");

        try (ConfigurableApplicationContext ignored = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=smoke.env",
            "--springdotenv.exportToSystemProperties=false"
        )) {
            assertThat(System.getProperty("DOTENV_ONLY")).isNull();
        }
    }
}
