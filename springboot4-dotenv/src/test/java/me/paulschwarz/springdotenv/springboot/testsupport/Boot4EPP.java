package me.paulschwarz.springdotenv.springboot.testsupport;

import static org.apache.commons.logging.LogFactory.getLog;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.bootstrap.ConfigurableBootstrapContext;
import org.springframework.boot.bootstrap.DefaultBootstrapContext;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.core.io.support.SpringFactoriesLoader;

public class Boot4EPP {

    public static List<EnvironmentPostProcessor> getEnvironmentPostProcessors() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        SpringFactoriesLoader loader = SpringFactoriesLoader.forDefaultResourceLocation(cl);

        DeferredLogFactory deferredLogFactory = new DeferredLogs();
        ConfigurableBootstrapContext bootstrapContext = new DefaultBootstrapContext();

        return loader.load(
            EnvironmentPostProcessor.class,

            new SpringFactoriesLoader.ArgumentResolver() {

                @Override
                public <T> T resolve(@NonNull Class<T> type) {
                    if (type.isInstance(deferredLogFactory)) {
                        return type.cast(deferredLogFactory);
                    }

                    if (type.isInstance(bootstrapContext)) {
                        return type.cast(bootstrapContext);
                    }

                    return null;
                }
            },
            SpringFactoriesLoader.FailureHandler.logging(getLog(Boot4TestAppRunner.class))
        );
    }
}
