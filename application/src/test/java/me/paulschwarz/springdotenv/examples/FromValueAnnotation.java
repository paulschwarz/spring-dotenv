package me.paulschwarz.springdotenv.examples;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FromValueAnnotation {

  @Value("${example.value1}")
  String value1;

  @Value("${example.value2}")
  String value2;

  @Value("${example.value3}")
  String value3;

  @Value("${EXAMPLE_MESSAGE_1}")
  String value1v2;

  @Value("${EXAMPLE_MESSAGE_2}")
  String value2v2;

  @Value("${EXAMPLE_MESSAGE_3}")
  String value3v2;

  public String getValue1() {
    return value1;
  }

  public String getValue2() {
    return value2;
  }

  public String getValue3() {
    return value3;
  }

  public String getValue1v2() { return value1v2; }

  public String getValue2v2() { return value2v2; }

  public String getValue3v2() { return value3v2; }
}
