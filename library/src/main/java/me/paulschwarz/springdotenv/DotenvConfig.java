package me.paulschwarz.springdotenv;

import java.util.Properties;

public class DotenvConfig {

  private final String prefix;
  private final String directory;
  private final String filename;
  private final boolean ignoreIfMalformed;
  private final boolean ignoreIfMissing;
  private final boolean systemProperties;
  private final boolean suppressPrefixDeprecationWarning;

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

  public String getDirectory() {
    return directory;
  }

  public String getFilename() {
    return filename;
  }

  public String getPrefix() {
    return prefix;
  }

  public boolean ignoreIfMalformed() {
    return ignoreIfMalformed;
  }

  public boolean ignoreIfMissing() {
    return ignoreIfMissing;
  }

  public boolean systemProperties() {
    return systemProperties;
  }

  public boolean suppressPrefixDeprecationWarning() {
    return suppressPrefixDeprecationWarning;
  }

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
