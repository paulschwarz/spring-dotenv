package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.springboot.testkit.Boot3TestAppRunner.run;
import static me.paulschwarz.springdotenv.testkit.ListUtils.firstIndexContaining;
import static org.apache.commons.logging.LogFactory.getLog;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import me.paulschwarz.springdotenv.springboot.testkit.Boot3TestAppRunner;
import me.paulschwarz.springdotenv.testkit.EmptyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.lang.NonNull;

class Boot3DotenvWiringTests {

    static List<EnvironmentPostProcessor> getEnvironmentPostProcessors() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        SpringFactoriesLoader loader = SpringFactoriesLoader.forDefaultResourceLocation(cl);

        DeferredLogFactory deferredLogFactory = new DeferredLogs();

        return loader.load(
            EnvironmentPostProcessor.class,
            new SpringFactoriesLoader.ArgumentResolver() {
                @Override
                public <T> T resolve(@NonNull Class<T> type) {
                    return type.isInstance(deferredLogFactory) ? type.cast(deferredLogFactory) : null;
                }
            },
            SpringFactoriesLoader.FailureHandler.logging(getLog(Boot3TestAppRunner.class))
        );
    }

    @Test
    void shouldBeDiscoverableViaSpringFactoriesLoader() {
        assertThat(getEnvironmentPostProcessors())
            .extracting(Object::getClass)
            .extracting(Class::getName)
            .contains("me.paulschwarz.springdotenv.springboot.Boot3DotenvEnvironmentPostProcessor");
    }

    @Test
    void shouldRegisterDotenvPropertySourceBeforeApplicationProperties() {
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
    void shouldAddDotenvPropertySourceExactlyOnce() {
        try (ConfigurableApplicationContext ctx = run(
            EmptyConfig.class,
            "--springdotenv.directory=dotenv",
            "--springdotenv.filename=precedence.env"
        )) {
            List<String> names = ctx.getEnvironment().getPropertySources().stream()
                .map(PropertySource::getName)
                .toList();

            long count = names.stream()
                .filter(n -> n.contains("dotenvPropertySource"))
                .count();

            assertThat(count)
                .as("Expected exactly one dotenvPropertySource but got %s (names=%s)", count, names)
                .isEqualTo(1);
        }
    }

    @Test
    void shouldMakeDotenvPropertiesAvailableDuringConfigurationPropertiesBinding() {
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
    record TestProps(String dotenvOnly) {}
}
