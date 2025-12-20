package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.springboot.testsupport.Boot3TestAppRunner.run;
import static org.assertj.core.api.Assertions.assertThat;

import me.paulschwarz.springdotenv.testkit.EmptyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

class Boot3PrecedenceCliOverridesDotenvTest {

    @Test
    void cliArg_overrides_dotenv() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=precedence.env",
            "--DOTENV_AND_CLI=from cli"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_AND_CLI")).isEqualTo("from cli");
        }
    }
}
