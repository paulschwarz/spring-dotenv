package examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootDemoApplication implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(SpringBootDemoApplication.class);

  @Value("${example.name}")
  String name;

  public static void main(String[] args) {
    SpringApplication.run(SpringBootDemoApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) {
    log.info("Hello, {}", name);
  }
}
