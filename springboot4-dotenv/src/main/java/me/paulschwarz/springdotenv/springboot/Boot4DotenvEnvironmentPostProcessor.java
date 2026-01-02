package me.paulschwarz.springdotenv.springboot;

import me.paulschwarz.springdotenv.DotenvConfigLoader;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

public final class Boot4DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final DotenvConfigLoader dotenvConfigLoader = new Boot4DotenvConfigLoader();

    @Override
    public void postProcessEnvironment(@NonNull ConfigurableEnvironment env, @NonNull SpringApplication application) {
        var dotenvConfig = dotenvConfigLoader.load(env);
        Boot4DotenvEnvironmentApplier.apply(env, dotenvConfig);
    }
}
