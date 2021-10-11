package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DotenvPropertySourceTest {

  private final DotenvPropertySource source = new DotenvPropertySource();

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
    assertThat(source.getProperty("env.EXAMPLE_MESSAGE_1")).isEqualTo("Message 1 from .env");
  }

  @Test
  void valueFromEnvironment() {
    assertThat(source.getProperty("env.EXAMPLE_MESSAGE_2")).isEqualTo("Message 2 from system environment");
  }

  @Test
  void valueFromEnvironmentOverride() {
    assertThat(source.getProperty("env.EXAMPLE_MESSAGE_3")).isEqualTo("Message 3 from system environment");
  }
}
