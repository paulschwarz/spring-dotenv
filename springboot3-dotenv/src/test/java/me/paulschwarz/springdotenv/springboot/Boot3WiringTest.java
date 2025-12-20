package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.springboot.testsupport.Boot3TestAppRunner.run;
import static me.paulschwarz.springdotenv.testkit.ListUtils.firstIndexContaining;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import me.paulschwarz.springdotenv.testkit.EmptyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;

class Boot3WiringTest {

    @Test
    void dotenvPropertySource_isBeforeApplicationProperties() {
        try (ConfigurableApplicationContext ctx = run(EmptyConfig.class)) {
            List<String> names = ctx.getEnvironment().getPropertySources().stream()
                .map(PropertySource::getName)
                .toList();

            int dotenvIndex = firstIndexContaining(names, "dotenvPropertySource");
            int appPropsIndex = firstIndexContaining(names, "application.properties");

            assertThat(dotenvIndex)
                .as("Expected 'dotenvPropertySource' but got: %s", names)
                .isGreaterThanOrEqualTo(0);

            assertThat(appPropsIndex)
                .as("Expected an application.properties source but got: %s", names)
                .isGreaterThanOrEqualTo(0);

            assertThat(dotenvIndex)
                .as("dotenvPropertySource should come before application.properties (names=%s)", names)
                .isLessThan(appPropsIndex);
        }
    }

    @Test
    void dotenv_isAvailableDuringConfigurationPropertiesBinding() {
        try (ConfigurableApplicationContext ctx = run(
            TestApp.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=precedence.env"
        )) {
            TestProps props = ctx.getBean(TestProps.class);

            assertThat(props.dotenvOnly()).isEqualTo("from dotenv");
        }
    }

    @EnableConfigurationProperties(TestProps.class)
    static class TestApp {}

    @ConfigurationProperties(prefix = "test")
    record TestProps(
        String dotenvOnly
    ) {}
}
