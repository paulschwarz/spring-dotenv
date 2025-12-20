package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.springboot.testsupport.Boot3EPP.getEnvironmentPostProcessors;
import static me.paulschwarz.springdotenv.springboot.testsupport.Boot3TestAppRunner.run;
import static org.assertj.core.api.Assertions.assertThat;

import me.paulschwarz.springdotenv.testkit.EmptyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

class Boot3EnvironmentPostProcessorTest {

    @Test
    void isDiscoverableViaSpringFactoriesLoader() {
        assertThat(getEnvironmentPostProcessors())
            .extracting(Object::getClass)
            .extracting(Class::getName)
            .contains("me.paulschwarz.springdotenv.springboot.Boot3DotenvEnvironmentPostProcessor");
    }

    @Test
    void when_envAndDotenv_present_then_envWins() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=precedence.env"
        )) {
            assertThat(ctx.getEnvironment().getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
            assertThat(ctx.getEnvironment().getProperty("DOTENV_AND_ENV")).isEqualTo("from env");
        }
    }
}
