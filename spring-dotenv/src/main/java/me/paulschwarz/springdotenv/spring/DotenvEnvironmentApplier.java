package me.paulschwarz.springdotenv.spring;

import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import java.util.Optional;

public final class DotenvEnvironmentApplier {

    private static final Log log = LogFactory.getLog(DotenvEnvironmentApplier.class);

    private DotenvEnvironmentApplier() {}

    public static void apply(ConfigurableEnvironment env, DotenvConfig dotenvConfig) {
        boolean enabled = Optional
            .ofNullable(env.getProperty("springdotenv.enabled", Boolean.class))
            .orElse(dotenvConfig.enabled());

        log.debug("springdotenv.enabled=" + enabled);

        if (enabled) {
            DotenvPropertySource.addToEnvironment(env, dotenvConfig);
        }
    }
}
