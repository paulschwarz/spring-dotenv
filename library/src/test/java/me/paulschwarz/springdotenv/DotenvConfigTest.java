package me.paulschwarz.springdotenv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Properties;
import org.junit.jupiter.api.Test;

class DotenvConfigTest {

  @Test
  void defaults() {
    DotenvConfig dotenvConfig = new DotenvConfig(new Properties());

    assertNull(dotenvConfig.getDirectory());
    assertNull(dotenvConfig.getFilename());
    assertFalse(dotenvConfig.ignoreIfMalformed());
    assertTrue(dotenvConfig.ignoreIfMissing());
    assertFalse(dotenvConfig.systemProperties());
    assertNull(dotenvConfig.getPrefix());
  }

  @Test
  void withValues() {
    Properties properties = mock(Properties.class);

    doReturn("./some/path").when(properties).getProperty("directory");
    doReturn(".env").when(properties).getProperty("filename");
    doReturn("true").when(properties).getProperty("ignoreIfMalformed", "false");
    doReturn("false").when(properties).getProperty("ignoreIfMissing", "true");
    doReturn("true").when(properties).getProperty("systemProperties", "false");
    doReturn("").when(properties).getProperty("prefix");

    DotenvConfig dotenvConfig = new DotenvConfig(properties);

    assertEquals("./some/path", dotenvConfig.getDirectory());
    assertEquals(".env", dotenvConfig.getFilename());
    assertTrue(dotenvConfig.ignoreIfMalformed());
    assertFalse(dotenvConfig.ignoreIfMissing());
    assertTrue(dotenvConfig.systemProperties());
    assertEquals("", dotenvConfig.getPrefix());
  }
}
