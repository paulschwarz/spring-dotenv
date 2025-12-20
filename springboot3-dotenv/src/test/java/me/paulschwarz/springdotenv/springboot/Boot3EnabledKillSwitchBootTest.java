package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.springboot.testsupport.Boot3TestAppRunner.run;
import static org.assertj.core.api.Assertions.assertThat;

import me.paulschwarz.springdotenv.testkit.EmptyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

class Boot3EnabledKillSwitchBootTest {

    @Test
    void when_enabled_dotenv_is_loaded() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=smoke.env",
            "--springdotenv.enabled=true"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY"))
                .isEqualTo("from dotenv");
        }
    }

    @Test
    void when_disabled_dotenv_is_not_loaded() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=smoke.env",
            "--springdotenv.enabled=false"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY"))
                .isNull();
        }
    }
}
