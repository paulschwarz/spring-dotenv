package me.paulschwarz.springdotenv;

import java.util.Objects;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.log.LogMessage;

/**
 * A {@link PropertySource} that reads properties from a dotenv file.
 */
public class DotenvPropertySource extends PropertySource<DotenvPropertyLoader> {

  private static final Log log = LogFactory.getLog(DotenvPropertySource.class);

  /**
   * Name of the env {@link PropertySource}.
   */
  public static final String DOTENV_PROPERTY_SOURCE_NAME = "env";

  private static final String DEFAULT_PREFIX = "";

  private String prefix;

  /**
   * Create a new instance of {@link DotenvPropertySource}.
   *
   * @param name the name of the property source
   * @param dotenvConfig the configuration for the dotenv file
   */
  public DotenvPropertySource(String name, DotenvConfig dotenvConfig) {
    super(name, new DotenvPropertyLoader(dotenvConfig));
  }

  /**
   * Create a new instance of {@link DotenvPropertySource}.
   *
   * @param dotenvConfig the configuration for the dotenv file
   */
  public DotenvPropertySource(DotenvConfig dotenvConfig) {
    this(DOTENV_PROPERTY_SOURCE_NAME, dotenvConfig);

    prefix = Optional.ofNullable(dotenvConfig.getPrefix()).orElse(DEFAULT_PREFIX);

    if (!prefix.isEmpty()) {
      LogMessage warning = LogMessage.format("spring-dotenv: Using a prefix is DEPRECATED as of spring-dotenv version 3.%n" +
          "spring-dotenv: You are using the prefix \"%1$s\".%n" +
          "spring-dotenv: Please convert all usages of ${%1$sEXAMPLE} to ${EXAMPLE} " +
          "and remove prefix=%1$s from your .env.properties file.", prefix);

      if (dotenvConfig.suppressPrefixDeprecationWarning()) {
        log.warn(warning);
      } else {
        System.err.println(warning);
      }
    }
  }

  /**
   * Return the value associated with the given name, or {@code null} if not found.
   *
   * @param name the property to find
   * @see PropertyResolver#getRequiredProperty(String)
   */
  @Override
  public Object getProperty(String name) {
    if (!name.startsWith(prefix)) {
      return null;
    }

    Object value = getSource().getValue(name.substring(prefix.length()));

    if (Objects.nonNull(value)) {
      log.trace(LogMessage.format("Got env property for \"%s\"", name));
    }

    return value;
  }

  /**
   * Add the {@link DotenvPropertySource} to the given Spring's {@link ConfigurableEnvironment}.
   *
   * @param environment the environment to add the {@link DotenvPropertySource} to
   */
  public static void addToEnvironment(ConfigurableEnvironment environment) {
    DotenvConfig dotenvConfig = new DotenvConfig(DotenvConfigProperties.loadProperties());
    DotenvPropertySource dotenvPropertySource = new DotenvPropertySource(dotenvConfig);

    log.info(LogMessage.format("Initialized Dotenv with %s", dotenvConfig));

    if (dotenvConfig.systemProperties()) {
      log.trace("Dotenv environment available as system properties");
    } else {
      environment.getPropertySources()
          .addAfter(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, dotenvPropertySource);

      log.trace("DotenvPropertySource added to Environment");
    }
  }
}
