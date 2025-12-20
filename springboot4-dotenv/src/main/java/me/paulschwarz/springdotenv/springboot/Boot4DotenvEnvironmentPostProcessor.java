package me.paulschwarz.springdotenv.springboot;

import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.spring.DotenvConfigExtractor;
import me.paulschwarz.springdotenv.spring.DotenvEnvironmentApplier;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import java.util.Map;

public final class Boot4DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

  @Override
  public void postProcessEnvironment(@NonNull ConfigurableEnvironment env, @NonNull SpringApplication application) {
      Map<String, String> config = DotenvConfigExtractor.from(env);
      DotenvConfig cfg = DotenvConfig.load(config); // sees command-line args

      DotenvEnvironmentApplier.apply(env, cfg);
  }
}
