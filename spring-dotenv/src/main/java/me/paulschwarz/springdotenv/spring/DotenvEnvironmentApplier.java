package me.paulschwarz.springdotenv.spring;

import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;

public final class DotenvEnvironmentApplier {

    private static final Log log = LogFactory.getLog(DotenvEnvironmentApplier.class);

    private DotenvEnvironmentApplier() {}

    public static void apply(ConfigurableEnvironment env, DotenvConfig dotenvConfig) {
        if (dotenvConfig.enabled()) {
            DotenvPropertySource.addToEnvironment(env, dotenvConfig);
        } else {
            log.debug("Spring Dotenv is disabled (springdotenv.enabled=false)");
        }
    }
}
