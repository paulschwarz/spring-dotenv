package me.paulschwarz.springdotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;

class DotenvConfigProperties {

  private DotenvConfigProperties() {
  }

  private static final Log log = LogFactory.getLog(DotenvConfigProperties.class);
  private static final String PROPERTIES_FILE = ".env.properties";

  static Properties loadProperties() {
    Properties properties = new Properties();

    try (InputStream input = DotenvConfigProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
      if (Objects.isNull(input)) {
        log.debug(LogMessage.format("Property file not found \"%s\", falling back to defaults", PROPERTIES_FILE));

        return new Properties();
      }

      properties.load(input);
    } catch (IOException e) {
      log.warn(LogMessage.format("Error loading property file \"%s\", falling back to defaults. Caused by: %s", PROPERTIES_FILE, e.getMessage()));
    }

    return properties;
  }
}
