package me.paulschwarz.springdotenv;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.core.log.LogMessage;
import org.springframework.lang.NonNull;

public class DotenvPropertySource extends PropertySource<DotenvPropertyLoader> {

    private static final Log log = LogFactory.getLog(DotenvPropertySource.class);

    /**
     * Name of the strict dotenv {@link PropertySource} (plain Spring behavior).
     */
    public static final String DOTENV_PROPERTY_SOURCE_NAME = "dotenvPropertySource";

    /**
     * Name suffix to trigger Spring Boot env-var style mapping rules.
     * @see <a href="https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties.relaxed-binding.environment-variables">Binding From Environment Variables</a>
     */
    public static final String DOTENV_SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME =
        DOTENV_PROPERTY_SOURCE_NAME + "-"
            + StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME; // "dotenvPropertySource-systemEnvironment"

    public DotenvPropertySource(String name, DotenvConfig dotenvConfig) {
        super(name, new DotenvPropertyLoader(dotenvConfig));
    }

    public DotenvPropertySource(DotenvConfig dotenvConfig) {
        this(DOTENV_PROPERTY_SOURCE_NAME, dotenvConfig);
    }

    /**
     * Return the value associated with the given name, or {@code null} if not found.
     *
     * @param name the property to find
     * @see PropertyResolver#getRequiredProperty(String)
     */
    @Override
    public Object getProperty(@NonNull String name) {
        Object value = getSource().getValue(name); // strict lookup

        if (Objects.nonNull(value)) {
            log.trace(LogMessage.format("Got env property for \"%s\"", name));
        }

        return value;
    }

    /**
     * Plain Spring behavior: strict keys (no Boot relaxed binding).
     */
    public static void addToEnvironment(ConfigurableEnvironment environment, DotenvConfig dotenvConfig) {
        addToEnvironmentInternal(
            environment,
            dotenvConfig,
            DOTENV_PROPERTY_SOURCE_NAME,
            () -> new DotenvPropertySource(dotenvConfig));
    }

    /**
     * Spring Boot behavior: expose dotenv entries as an env-var style property source. This enables proper
     * environment-variable binding rules (dots/dashes/indexes) in Boot.
     */
    public static void addToEnvironmentAsSystemEnvironment(ConfigurableEnvironment environment, DotenvConfig dotenvConfig) {
        addToEnvironmentInternal(
            environment,
            dotenvConfig,
            DOTENV_SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
            () -> {
                DotenvPropertyLoader loader = new DotenvPropertyLoader(dotenvConfig);
                Map<String, Object> map = loader.asMap();
                return new SystemEnvironmentPropertySource(DOTENV_SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, map);
            });
    }

    private static void addToEnvironmentInternal(
        ConfigurableEnvironment environment,
        DotenvConfig dotenvConfig,
        String propertySourceName,
        Supplier<PropertySource<?>> propertySourceFactory
    ) {
        var sources = environment.getPropertySources();

        if (sources.contains(propertySourceName)) {
            log.warn("A property source named " + propertySourceName
                + " is already present in the environment. Duplicate registration was skipped; please verify"
                + " configuration to avoid redundant initialization.");
            return;
        }

        log.info(LogMessage.format("Initialized Dotenv with %s", dotenvConfig));

        if (dotenvConfig.exportToSystemProperties()) {
            new DotenvPropertyLoader(dotenvConfig); // Unfortunate constructor magic: triggers load() and export
            log.trace("Dotenv environment available as system properties");
            return;
        }

        PropertySource<?> propertySource = propertySourceFactory.get();

        if (sources.contains(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)) {
            sources.addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, propertySource);
        } else if (sources.contains(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME)) {
            sources.addAfter(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, propertySource);
        } else {
            sources.addFirst(propertySource);
        }

        log.trace("Dotenv PropertySource added to Environment: " + propertySourceName);
    }
}
