package me.paulschwarz.springdotenv;

import java.util.Objects;
import java.util.Optional;
import org.springframework.core.env.ConfigurableEnvironment;

public class DotenvConfig {

  private static final String CONFIG_PREFIX = ".env.";

  private String directory;
  private String filename;
  private Boolean ignoreIfMalformed;
  private Boolean ignoreIfMissing = true;
  private Boolean systemProperties;
  private String prefixProperty;

  public DotenvConfig(ConfigurableEnvironment environment) {
    directory = getEnvironmentProperty(environment, "directory", directory);
    filename = getEnvironmentProperty(environment, "filename", filename);
    ignoreIfMalformed = getEnvironmentProperty(environment, "ignoreIfMalformed", ignoreIfMalformed);
    ignoreIfMissing = getEnvironmentProperty(environment, "ignoreIfMissing", ignoreIfMissing);
    systemProperties = getEnvironmentProperty(environment, "systemProperties", systemProperties);
    prefixProperty = getEnvironmentProperty(environment, "prefixProperty", prefixProperty);
  }

  private String getEnvironmentProperty(ConfigurableEnvironment environment, String key, String defaultValue) {
    if (Objects.isNull(environment)) {
      return defaultValue;
    }

    return environment.getProperty(CONFIG_PREFIX + key, defaultValue);
  }

  private boolean getEnvironmentProperty(ConfigurableEnvironment environment, String key, Boolean defaultValue) {
    String defaultValueAsString = String.valueOf(defaultValue != null && defaultValue);
    String valueAsString = getEnvironmentProperty(environment, key, defaultValueAsString);
    return Boolean.parseBoolean(valueAsString);
  }

  public Optional<String> getDirectoryOptional() {
    return Optional.ofNullable(directory);
  }

  public Optional<String> getFilenameOptional() {
    return Optional.ofNullable(filename);
  }

  public Optional<String> getPrefixPropertyOptional() {
    return Optional.ofNullable(prefixProperty);
  }

  public Optional<Boolean> getIgnoreIfMalformedTruth() {
    return Optional.ofNullable(ignoreIfMalformed)
        .filter(Boolean::booleanValue);
  }

  public Optional<Boolean> getIgnoreIfMissingTruth() {
    return Optional.ofNullable(ignoreIfMissing)
        .filter(Boolean::booleanValue);
  }

  public Optional<Boolean> getSystemPropertiesTruth() {
    return Optional.ofNullable(systemProperties)
        .filter(Boolean::booleanValue);
  }

  @Override
  public String toString() {
    return "DotenvConfig{" +
        "directory=" + getDirectoryOptional().map(value -> "'" + value + "'").orElse("null") +
        ", filename=" + getFilenameOptional().map(value -> "'" + value + "'").orElse("null") +
        ", ignoreIfMalformed=" + ignoreIfMalformed +
        ", ignoreIfMissing=" + ignoreIfMissing +
        ", systemProperties=" + systemProperties +
        ", prefixProperty=" + prefixProperty +
        '}';
  }
}
