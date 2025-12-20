package integration;

import me.paulschwarz.springdotenv.spring.DotenvApplicationInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class DotenvApplicationInitializerSpringTest {

    @Test
    void dotenv_is_visible_to_value_injection_in_plain_spring() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            new DotenvApplicationInitializer().initialize(ctx);

            ctx.register(TestConfig.class);
            ctx.refresh();

            TestConfig.Greeter greeter = ctx.getBean(TestConfig.Greeter.class);
            assertThat(greeter.hello()).isEqualTo("Hello World");
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
