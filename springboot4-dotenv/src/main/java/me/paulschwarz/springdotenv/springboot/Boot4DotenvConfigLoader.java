package me.paulschwarz.springdotenv.springboot;

import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.DotenvConfigLoader;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;

public class Boot4DotenvConfigLoader implements DotenvConfigLoader {

    @Override
    public DotenvConfig load(ConfigurableEnvironment env) {
        Boot4DotenvProperties props = Binder.get(env)
            .bind(SPRINGDOTENV, Bindable.of(Boot4DotenvProperties.class))
            .orElseGet(Boot4DotenvProperties::new);

        return new DotenvConfig(
            props.isEnabled(),
            props.getDirectory(),
            props.getFilename(),
            props.isIgnoreIfMalformed(),
            props.isIgnoreIfMissing(),
            props.isExportToSystemProperties());
    }
}
