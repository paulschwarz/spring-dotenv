package me.paulschwarz.springdotenv;

import org.springframework.core.env.ConfigurableEnvironment;

public interface DotenvConfigLoader {

    String SPRINGDOTENV = "springdotenv";

    DotenvConfig load(ConfigurableEnvironment env);
}
