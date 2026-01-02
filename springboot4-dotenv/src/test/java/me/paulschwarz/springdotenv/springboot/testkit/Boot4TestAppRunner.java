package me.paulschwarz.springdotenv.springboot.testkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

public final class Boot4TestAppRunner {

    private Boot4TestAppRunner() {}

    public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
        SpringApplication app = new SpringApplication(primarySource);
        app.setWebApplicationType(WebApplicationType.NONE);

        return app.run(args);
    }
}
