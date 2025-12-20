package me.paulschwarz.springdotenv.springboot;

import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.spring.DotenvConfigExtractor;
import me.paulschwarz.springdotenv.spring.DotenvEnvironmentApplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import java.util.Map;

public final class Boot3DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
      Map<String, String> config = DotenvConfigExtractor.from(env);
      DotenvConfig cfg = DotenvConfig.load(config); // sees command-line args

      DotenvEnvironmentApplier.apply(env, cfg);
  }
}
