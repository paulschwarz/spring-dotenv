package me.paulschwarz.springdotenv.examples;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("example")
public class FromProperties {

  String value1;
  String value2;
  String value3;

  public void setValue1(String value1) {
    this.value1 = value1;
  }

  public void setValue2(String value2) {
    this.value2 = value2;
  }

  public void setValue3(String value3) {
    this.value3 = value3;
  }

  public String getValue1() {
    return value1;
  }

  public String getValue2() {
    return value2;
  }

  public String getValue3() {
    return value3;
  }
}
