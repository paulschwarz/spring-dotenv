package me.paulschwarz.springdotenv.spring;

import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.DotenvConfigLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class DotenvApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final DotenvConfigLoader dotenvConfigLoader = new SpringDotenvConfigLoader();

    /**
     * Initialize the given application context.
     *
     * @param ctx the application to configure
     */
    @Override
    public void initialize(final ConfigurableApplicationContext ctx) {
        ConfigurableEnvironment env = ctx.getEnvironment();
        DotenvConfig dotenvConfig = dotenvConfigLoader.load(env);
        DotenvEnvironmentApplier.apply(env, dotenvConfig);
    }
}
