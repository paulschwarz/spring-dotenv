package me.paulschwarz.springdotenv.springboot;

import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;

public final class Boot4DotenvEnvironmentApplier {

    private static final Log log = LogFactory.getLog(Boot4DotenvEnvironmentApplier.class);

    private Boot4DotenvEnvironmentApplier() {}

    public static void apply(ConfigurableEnvironment env, DotenvConfig dotenvConfig) {
        if (dotenvConfig.enabled()) {
            DotenvPropertySource.addToEnvironmentAsSystemEnvironment(env, dotenvConfig);
        } else {
            log.debug("Spring Dotenv is disabled (springdotenv.enabled=false)");
        }
    }
}
