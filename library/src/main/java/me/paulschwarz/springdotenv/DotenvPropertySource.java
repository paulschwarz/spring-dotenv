package me.paulschwarz.springdotenv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

public class DotenvPropertySource extends PropertySource<DotenvPropertyLoader> {

  private static final Log logger = LogFactory.getLog(DotenvPropertySource.class);

  /** Name of the env {@link PropertySource}. */
  public static final String DOTENV_PROPERTY_SOURCE_NAME = "env";

  private static final String PREFIX = "env.";

  public DotenvPropertySource(String name, DotenvConfig dotenvConfig) {
    super(name, new DotenvPropertyLoader(dotenvConfig));
  }

  public DotenvPropertySource(DotenvConfig dotenvConfig) {
    this(DOTENV_PROPERTY_SOURCE_NAME, dotenvConfig);
  }

  public DotenvPropertySource() {
    this(new DotenvConfig(null));
  }

  /**
   * Return the value associated with the given name, or {@code null} if not found.
   *
   * @param name the property to find
   * @see PropertyResolver#getRequiredProperty(String)
   */
  @Override
  public Object getProperty(String name) {
    if (!name.startsWith(PREFIX)) {
      return null;
    }

    if (logger.isTraceEnabled()) {
      logger.trace("Getting env property for '" + name + "'");
    }

    return getSource().getValue(name.substring(PREFIX.length()));
  }

  public static void addToEnvironment(ConfigurableEnvironment environment) {
    DotenvConfig dotenvConfig = new DotenvConfig(environment);
    logger.info("Initializing Dotenv with " + dotenvConfig);

    environment
        .getPropertySources()
        .addAfter(
            StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
            new DotenvPropertySource(dotenvConfig));

    logger.trace("DotenvPropertySource added to Environment");
  }
}
