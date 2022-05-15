package me.paulschwarz.springdotenv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;

class DotenvConfigTest {

  @Test
  void defaults() {
    DotenvConfig dotenvConfig = new DotenvConfig(null);

    assertFalse(dotenvConfig.getDirectoryOptional().isPresent());
    assertFalse(dotenvConfig.getFilenameOptional().isPresent());
    assertFalse(dotenvConfig.getIgnoreIfMalformedTruth().isPresent());
    assertTrue(dotenvConfig.getIgnoreIfMissingTruth().isPresent());
    assertFalse(dotenvConfig.getSystemPropertiesTruth().isPresent());
    assertTrue(dotenvConfig.getIgnoreIfMissingTruth().get());
    assertFalse(dotenvConfig.getPrefixOptional().isPresent());

  }

  @Test
  void withValues() {
    ConfigurableEnvironment environment = mock(ConfigurableEnvironment.class);

    doReturn("./some/path").when(environment).getProperty(".env.directory", (String) null);
    doReturn(".env").when(environment).getProperty(".env.filename", (String) null);
    doReturn("true").when(environment).getProperty(".env.ignoreIfMalformed", "false");
    doReturn("false").when(environment).getProperty(".env.ignoreIfMissing", "true");
    doReturn("true").when(environment).getProperty(".env.systemProperties", "false");
    doReturn("").when(environment).getProperty(".env.prefix", (String) null);

    DotenvConfig dotenvConfig = new DotenvConfig(environment);

    assertTrue(dotenvConfig.getDirectoryOptional().isPresent());
    assertTrue(dotenvConfig.getFilenameOptional().isPresent());
    assertTrue(dotenvConfig.getIgnoreIfMalformedTruth().isPresent());
    assertFalse(dotenvConfig.getIgnoreIfMissingTruth().isPresent());
    assertTrue(dotenvConfig.getSystemPropertiesTruth().isPresent());
    assertTrue(dotenvConfig.getPrefixOptional().isPresent());

    assertEquals("./some/path", dotenvConfig.getDirectoryOptional().get());
    assertEquals(".env", dotenvConfig.getFilenameOptional().get());
    assertTrue(dotenvConfig.getIgnoreIfMalformedTruth().get());
    assertTrue(dotenvConfig.getSystemPropertiesTruth().get());

    assertEquals("", dotenvConfig.getPrefixOptional().get());
  }
}
