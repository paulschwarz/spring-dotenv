package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DotenvPropertySourceTest {

  private DotenvPropertySource source;

  @BeforeEach
  void setUp() {
    DotenvConfig dotenvConfig = mock(DotenvConfig.class);
    source = new DotenvPropertySource(dotenvConfig);
  }

  @Test
  void irrelevant() {
    assertThat(source.getProperty("other.VALUE")).isNull();
  }

  @Test
  void missing() {
    assertThat(source.getProperty("env.MISSING")).isNull();
  }

  @Test
  void valueFromDotenv() {
    assertThat(source.getProperty("EXAMPLE_MESSAGE_1")).isEqualTo("Message 1 from .env");
  }

  @Test
  void valueFromEnvironment() {
    assertThat(source.getProperty("EXAMPLE_MESSAGE_2")).isEqualTo("Message 2 from system environment");
  }

  @Test
  void valueFromEnvironmentOverride() {
    assertThat(source.getProperty("EXAMPLE_MESSAGE_3")).isEqualTo("Message 3 from system environment");
  }

  @Test
  void valueFromEnvironmentAsLowerCaseWithDots() {
    assertThat(source.getProperty("example.message.2")).isEqualTo("Message 2 from system environment");
  }

  @Test
  void valueFromEnvironmentAsLowerCaseWithDashes() {
    assertThat(source.getProperty("example-message-2")).isEqualTo("Message 2 from system environment");
  }

  @Test
  void valueFromEnvironmentAsLowerCaseWithDotsAndDashes() {
    assertThat(source.getProperty("example.message-2")).isEqualTo("Message 2 from system environment");
  }
}
