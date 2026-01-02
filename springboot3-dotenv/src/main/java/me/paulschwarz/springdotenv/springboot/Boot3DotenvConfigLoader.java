package me.paulschwarz.springdotenv.springboot;

import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.DotenvConfigLoader;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;

public class Boot3DotenvConfigLoader implements DotenvConfigLoader {

    @Override
    public DotenvConfig load(ConfigurableEnvironment env) {
        Boot3DotenvProperties props = Binder.get(env)
            .bind(SPRINGDOTENV, Bindable.of(Boot3DotenvProperties.class))
            .orElseGet(Boot3DotenvProperties::new);

        return new DotenvConfig(
            props.isEnabled(),
            props.getDirectory(),
            props.getFilename(),
            props.isIgnoreIfMalformed(),
            props.isIgnoreIfMissing(),
            props.isExportToSystemProperties());
    }
}
