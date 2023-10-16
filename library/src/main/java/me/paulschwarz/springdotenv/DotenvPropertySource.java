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

public class DotenvPropertySource extends PropertySource<DotenvPropertyLoader> {

  private static final Log log = LogFactory.getLog(DotenvPropertySource.class);

  /**
   * Name of the env {@link PropertySource}.
   */
  public static final String DOTENV_PROPERTY_SOURCE_NAME = "env";

  private static final String DEFAULT_PREFIX = "";

  private String prefix;

  public DotenvPropertySource(String name, DotenvConfig dotenvConfig) {
    super(name, new DotenvPropertyLoader(dotenvConfig));
  }

  public DotenvPropertySource(DotenvConfig dotenvConfig) {
    this(DOTENV_PROPERTY_SOURCE_NAME, dotenvConfig);

    prefix = Optional.ofNullable(dotenvConfig.getPrefix()).orElse(DEFAULT_PREFIX);

    if (!"".equals(prefix)) {
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
   * Handles Spring Boot relaxed binding by allowing multiple naming conventions
   * for the property name (omitting the preifx):
   * <ul>
   *   <li>requested name</li>
   *   <li>requested name replacing dots, dashes or both by underscores</li>
   *   <li>requested name in uppercase</li>
   *   <li>requested name in uppercase replacing dots, dashes or both by underscores</li>
   * </ul>
   *
   * @param name the property to find
   * @see PropertyResolver#getRequiredProperty(String)
   */
  @Override
  public Object getProperty(String name) {
    if (!name.startsWith(prefix)) {
      return null;
    }

    String noPrefix = name.substring(prefix.length());

    String actualName = resolvePropertyName(noPrefix);
    if (log.isTraceEnabled() && !name.equals(actualName)) {
      logger.trace(LogMessage.format("PropertySource \"%s\" does not contain " +
          "property \"%s\", but found equivalent \"%s\"", getName(), name, actualName));
    }
    Object value = getSource().getValue(actualName);

    if (Objects.nonNull(value) && log.isTraceEnabled()) {
      log.trace(LogMessage.format("Got env property for \"%s\"", name));
    }

    return value;
  }

  protected final String resolvePropertyName(String name) {
    Objects.requireNonNull(name, "Property name must not be null");
    String resolvedName = checkPropertySymbolVariants(name);
    if (resolvedName != null) {
      return resolvedName;
    }
    String uppercaseName = name.toUpperCase();
    if (!name.equals(uppercaseName)) {
      resolvedName = checkPropertySymbolVariants(uppercaseName);
      if (resolvedName != null) {
        return resolvedName;
      }
    }
    return name;
  }

  @Nullable
  private String checkPropertySymbolVariants(String name) {
    // Check name as-is
    if (this.getSource().containsProperty(name)) {
      return name;
    }
    // Check name with just dots replaced
    String noDotName = name.replace('.', '_');
    if (!name.equals(noDotName) && this.source.containsProperty(noDotName)) {
      return noDotName;
    }
    // Check name with just hyphens replaced
    String noHyphenName = name.replace('-', '_');
    if (!name.equals(noHyphenName) && this.source.containsProperty(noHyphenName)) {
      return noHyphenName;
    }
    // Check name with dots and hyphens replaced
    String noDotNoHyphenName = noDotName.replace('-', '_');
    if (!noDotName.equals(noDotNoHyphenName) && this.source.containsProperty(noDotNoHyphenName)) {
      return noDotNoHyphenName;
    }
    // Give up
    return null;
  }

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
