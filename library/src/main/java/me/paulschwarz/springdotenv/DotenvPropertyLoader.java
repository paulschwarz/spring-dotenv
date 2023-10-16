package me.paulschwarz.springdotenv;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import java.util.Optional;

public class DotenvPropertyLoader {

  private final Dotenv dotenv;

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

  public Object getValue(String key) {
    return dotenv.get(key);
  }

  boolean containsProperty(String key) {
    return getValue(key) != null;
  }
}
