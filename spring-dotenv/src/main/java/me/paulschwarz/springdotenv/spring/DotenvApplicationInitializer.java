package me.paulschwarz.springdotenv.spring;

import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class DotenvApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * Initialize the given application context.
     *
     * @param ctx the application to configure
     */
    @Override
    public void initialize(final ConfigurableApplicationContext ctx) {
        DotenvPropertySource.addToEnvironment(ctx.getEnvironment());
    }
}
