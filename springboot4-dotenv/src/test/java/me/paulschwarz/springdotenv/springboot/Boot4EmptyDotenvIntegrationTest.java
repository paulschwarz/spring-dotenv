package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.springboot.testsupport.Boot4TestAppRunner.run;
import static org.assertj.core.api.Assertions.assertThat;

import me.paulschwarz.springdotenv.testkit.EmptyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

class Boot4EmptyDotenvIntegrationTest {

    @Test
    void emptyDotenv_doesNotAffectEnvironmentResolution() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=empty.env"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_AND_ENV")).isEqualTo("from env");
        }
    }
}
