package integration;

import me.paulschwarz.springdotenv.spring.DotenvApplicationInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

public class RelaxedBindingTest {

    @Test
    void shouldResolveDotenvValues_withRelaxedBinding_whenUsingPlainSpring() {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            new DotenvApplicationInitializer().initialize(ctx);

            ctx.register(RelaxedKeyConfig.class);
            ctx.refresh();

            RelaxedKeyConfig.Greeter greeter = ctx.getBean(RelaxedKeyConfig.Greeter.class);
            assertThat(greeter.hello()).isEqualTo("Hello World");
        }
    }

    @Configuration
    static class RelaxedKeyConfig {

        @Bean
        Greeter greeter(
            @Value("${greeting:missing}") String lowerCase,
            @Value("${GREETING:missing}") String exact,
            @Value("${greet_ing:missing}") String underscoreVariant,
            @Value("${greet-ing:missing}") String hyphenVariant
        ) {
            // any one of these resolving proves relaxed lookup;
            // we also assert they all agree for extra punch.
            assertThat(lowerCase).isEqualTo("Hello World");
            assertThat(exact).isEqualTo("Hello World");
            assertThat(underscoreVariant).isEqualTo("Hello World");
            assertThat(hyphenVariant).isEqualTo("Hello World");
            return new Greeter(lowerCase);
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
