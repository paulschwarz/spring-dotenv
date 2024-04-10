package me.paulschwarz.springdotenv;

import java.util.Properties;

/**
 * Configuration for the dotenv file.
 */
public class DotenvConfig {

  private final String prefix;
  private final String directory;
  private final String filename;
  private final boolean ignoreIfMalformed;
  private final boolean ignoreIfMissing;
  private final boolean systemProperties;
  private final boolean suppressPrefixDeprecationWarning;

  /**
   * Create a new instance of {@link DotenvConfig}.
   *
   * @param propertiesFile the env file
   */
  public DotenvConfig(Properties propertiesFile) {
    prefix = getStringProperty(propertiesFile, "prefix");
    directory = getStringProperty(propertiesFile, "directory");
    filename = getStringProperty(propertiesFile, "filename");
    ignoreIfMalformed = getBooleanProperty(propertiesFile, "ignoreIfMalformed", false);
    ignoreIfMissing = getBooleanProperty(propertiesFile, "ignoreIfMissing", true);
    systemProperties = getBooleanProperty(propertiesFile, "systemProperties", false);
    suppressPrefixDeprecationWarning = getBooleanProperty(propertiesFile, "suppressPrefixDeprecationWarning", false);
  }

  private static String getStringProperty(Properties propertiesFile, String key) {
    String value = propertiesFile.getProperty(key);

    if ("\"\"".equals(value)) {
      return "";
    }

    return value;
  }

  private static boolean getBooleanProperty(Properties propertiesFile, String key, boolean defaultValue) {
    return Boolean.parseBoolean(propertiesFile.getProperty(key, String.valueOf(defaultValue)));
  }

  /**
   * @return the directory containing the <code>.env</code> file.
   */
  public String getDirectory() {
    return directory;
  }

  /**
   * @return the filename providing the properties.
   */
  public String getFilename() {
    return filename;
  }

  /**
   * @return prefix to add to loaded properties. Defaults to empty.
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * @return <code>true</code> if Dotenv should ignore malformed entries.
   */
  public boolean ignoreIfMalformed() {
    return ignoreIfMalformed;
  }

  /**
   * @return <code>true</code> if Dotenv should ignore missing files. Defaults to <code>true</code>.
   */
  public boolean ignoreIfMissing() {
    return ignoreIfMissing;
  }

  /**
   * @return <code>true</code>, if the environment variables should be loaded into System properties.
   */
  public boolean systemProperties() {
    return systemProperties;
  }

  /**
   * @return the filename providing the properties.
   */
  public boolean suppressPrefixDeprecationWarning() {
    return suppressPrefixDeprecationWarning;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "DotenvConfig{" +
        "directory=" + directory +
        ", filename=" + filename +
        ", ignoreIfMalformed=" + ignoreIfMalformed +
        ", ignoreIfMissing=" + ignoreIfMissing +
        ", systemProperties=" + systemProperties +
        ", prefix=" + prefix +
        '}';
  }
}
