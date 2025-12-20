package me.paulschwarz.springdotenv.springboot.testsupport;

import static org.apache.commons.logging.LogFactory.getLog;

import java.util.List;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.lang.NonNull;

public class Boot3EPP {

    public static List<EnvironmentPostProcessor> getEnvironmentPostProcessors() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        SpringFactoriesLoader loader = SpringFactoriesLoader.forDefaultResourceLocation(cl);

        DeferredLogFactory deferredLogFactory = new DeferredLogs();

        return loader.load(
            EnvironmentPostProcessor.class,
            new SpringFactoriesLoader.ArgumentResolver() {
                @Override
                public <T> T resolve(@NonNull Class<T> type) {
                    return type.isInstance(deferredLogFactory) ? type.cast(deferredLogFactory) : null;
                }
            },
            SpringFactoriesLoader.FailureHandler.logging(getLog(Boot3TestAppRunner.class))
        );
    }
}
