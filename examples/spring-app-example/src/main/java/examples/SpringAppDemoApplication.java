package examples;

import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringAppDemoApplication {

  private static final Logger log = LoggerFactory.getLogger(SpringAppDemoApplication.class);

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

    // Add DotenvPropertySource to environment before registering components
    DotenvPropertySource.addToEnvironment(applicationContext.getEnvironment());

    applicationContext.register(Config.class);
    applicationContext.refresh();

    Config config = applicationContext.getBean(Config.class);
    log.info("Hello, {}", config.name);
  }
}
