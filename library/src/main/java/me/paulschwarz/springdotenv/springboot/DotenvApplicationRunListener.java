package me.paulschwarz.springdotenv.springboot;

import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Add the {@link DotenvPropertySource} to Spring's application environment.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DotenvApplicationRunListener implements SpringApplicationRunListener {

  /**
   * Create a new instance of {@link DotenvApplicationRunListener}.
   *
   * @param application the application
   * @param args the application's arguments
   */
  @SuppressWarnings("unused")
  public DotenvApplicationRunListener(SpringApplication application, String[] args) {
    // unused
  }

  /**
   * Add the {@link DotenvPropertySource} to the application's environment.
   *
   * @param bootstrapContext the bootstrap context
   * @param environment the environment
   */
  @Override
  public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
    DotenvPropertySource.addToEnvironment(environment);
    SpringApplicationRunListener.super.environmentPrepared(bootstrapContext, environment);
  }
}
