package me.paulschwarz.springdotenv;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;

public class DotenvPropertyLoader {

  private final Dotenv dotenv;

  public DotenvPropertyLoader(DotenvConfig dotenvConfig) {
    DotenvBuilder dotenvBuilder = Dotenv.configure();

    dotenvConfig.getDirectoryOptional().ifPresent(dotenvBuilder::directory);
    dotenvConfig.getFilenameOptional().ifPresent(dotenvBuilder::filename);
    dotenvConfig.getIgnoreIfMalformedTruth().ifPresent(value -> dotenvBuilder.ignoreIfMalformed());
    dotenvConfig.getIgnoreIfMissingTruth().ifPresent(value -> dotenvBuilder.ignoreIfMissing());
    dotenvConfig.getSystemPropertiesTruth().ifPresent(value -> dotenvBuilder.systemProperties());
    dotenv = dotenvBuilder.load();
  }

  public Object getValue(String key) {
    return dotenv.get(key);
  }
}
