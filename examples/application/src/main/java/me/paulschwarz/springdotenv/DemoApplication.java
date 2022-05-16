package me.paulschwarz.springdotenv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

  @Value("${example.name}")
  String name;

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) {
    logger.info("Hello, {}", name);
  }
}
