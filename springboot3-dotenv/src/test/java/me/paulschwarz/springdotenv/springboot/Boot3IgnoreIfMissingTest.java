package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.springboot.testsupport.Boot3TestAppRunner.run;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import me.paulschwarz.springdotenv.testkit.EmptyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

class Boot3IgnoreIfMissingTest {

    @Test
    void bootStartup_succeeds_whenDotenvMissing_andIgnoreIfMissingTrue() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=does-not-exist.env",
            "--springdotenv.ignoreIfMissing=true"
        )) {
            assertThat(ctx.getEnvironment().getProperty("springdotenv.ignoreIfMissing")).isEqualTo("true");
            assertThat(ctx.isActive()).isTrue();
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY")).isNull();
        }
    }

    @Test
    void when_missing_and_ignoreFalse_then_startupFails() {
        assertThatThrownBy(() -> run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=does-not-exist.env",
            "--springdotenv.ignoreIfMissing=false"
        ))
            .hasMessageContaining("Could not find")
            .hasMessageContaining("dotenv/does-not-exist.env");
    }
}
