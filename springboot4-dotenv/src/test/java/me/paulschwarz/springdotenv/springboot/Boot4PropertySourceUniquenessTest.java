package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.springboot.testsupport.Boot4TestAppRunner.run;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import me.paulschwarz.springdotenv.testkit.EmptyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;

class Boot4PropertySourceUniquenessTest {

    @Test
    void dotenvPropertySource_isAddedExactlyOnce() {
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
}
