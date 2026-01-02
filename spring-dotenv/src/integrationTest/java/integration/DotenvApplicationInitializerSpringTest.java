package integration;

import me.paulschwarz.springdotenv.spring.DotenvApplicationInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DotenvApplicationInitializerSpringTest {

    @Test
    void shouldInjectDotenvValues_whenUsingPlainSpring() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            new DotenvApplicationInitializer().initialize(ctx);

            ctx.register(TestConfig.class);
            ctx.refresh();

            TestConfig.Greeter greeter = ctx.getBean(TestConfig.Greeter.class);
            assertThat(greeter.hello()).isEqualTo("Hello World");
        }
    }

    @Test
    void shouldNotEnableDotenv_whenDisabledViaSystemProperty() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.getEnvironment().getPropertySources()
                .addFirst(new MapPropertySource("test", Map.of(
                    "springdotenv.enabled", "false"
                )));

            new DotenvApplicationInitializer().initialize(ctx);

            ctx.register(TestConfig.class);
            ctx.refresh();

            TestConfig.Greeter greeter = ctx.getBean(TestConfig.Greeter.class);
            assertThat(greeter.hello()).isEqualTo("missing");
        }
    }

    @Test
    void shouldNotEnableDotenv_whenDisabledViaEnvironmentVariable() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.getEnvironment().getPropertySources()
                .addFirst(new MapPropertySource("test", Map.of(
                    "SPRINGDOTENV_ENABLED", "false"
                )));

            new DotenvApplicationInitializer().initialize(ctx);

            ctx.register(TestConfig.class);
            ctx.refresh();

            TestConfig.Greeter greeter = ctx.getBean(TestConfig.Greeter.class);
            assertThat(greeter.hello()).isEqualTo("missing");
        }
    }

    @Configuration
    static class TestConfig {

        @Bean
        Greeter greeter(@Value("${GREETING:missing}") String greeting) {
            return new Greeter(greeting);
        }

        static class Greeter {
            private final String greeting;

            Greeter(String greeting) {
                this.greeting = greeting;
            }

            String hello() {
                return greeting;
            }
        }
    }
}
