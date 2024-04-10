package me.paulschwarz.springdotenv;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import java.util.Optional;

/**
 * Load properties from a dotenv.
 */
public class DotenvPropertyLoader {

  private final Dotenv dotenv;

  /**
   * Create a new instance of {@link DotenvPropertyLoader}.
   *
   * @param dotenvConfig the configuration for dotenv.
   */
  public DotenvPropertyLoader(DotenvConfig dotenvConfig) {
    DotenvBuilder dotenvBuilder = Dotenv.configure();

    Optional.ofNullable(dotenvConfig.getDirectory()).ifPresent(dotenvBuilder::directory);
    Optional.ofNullable(dotenvConfig.getFilename()).ifPresent(dotenvBuilder::filename);

    if (dotenvConfig.ignoreIfMalformed()) {
      dotenvBuilder.ignoreIfMalformed();
    }

    if (dotenvConfig.ignoreIfMissing()) {
      dotenvBuilder.ignoreIfMissing();
    }

    if (dotenvConfig.systemProperties()) {
      dotenvBuilder.systemProperties();
    }

    dotenv = dotenvBuilder.load();
  }

  /**
   * Get the value for a key.
   *
   * @param key the key
   * @return the value associated with <code>key</code>.
   */
  public Object getValue(String key) {
    return dotenv.get(key);
  }
}
