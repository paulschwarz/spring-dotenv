package me.paulschwarz.springdotenv.springboot;

import me.paulschwarz.springdotenv.DotenvConfigLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public final class Boot3DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final DotenvConfigLoader dotenvConfigLoader = new Boot3DotenvConfigLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        var dotenvConfig = dotenvConfigLoader.load(env);
        Boot3DotenvEnvironmentApplier.apply(env, dotenvConfig);
    }
}
