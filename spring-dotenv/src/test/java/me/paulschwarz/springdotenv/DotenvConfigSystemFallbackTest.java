package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;

import me.paulschwarz.springdotenv.testkit.SystemPropertiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

@ResourceLock(Resources.SYSTEM_PROPERTIES)
class DotenvConfigSystemFallbackTest {

    @RegisterExtension
    static final SystemPropertiesExtension sys = new SystemPropertiesExtension();

    @Test
    void verifyDotenvConfigOptions() {
        assertThat(DotenvConfig.ALL_KEYS).hasSize(8);
    }

    @Test
    void load_reads_system_properties_as_fallback() {
        sys.set("springdotenv.enabled", "false");
        sys.set("springdotenv.prefix", "env.");
        sys.set("springdotenv.directory", "dotenv");
        sys.set("springdotenv.filename", "smoke.env");
        sys.set("springdotenv.ignoreIfMissing", "false");
        sys.set("springdotenv.ignoreIfMalformed", "true");
        sys.set("springdotenv.systemProperties", "true");
        sys.set("springdotenv.suppressPrefixDeprecationWarning", "true");

        // Read in the sysprops/env/defaults
        DotenvConfig cfg = DotenvConfig.load();

        assertThat(cfg.enabled()).isFalse();
        assertThat(cfg.prefix()).isEqualTo("env.");
        assertThat(cfg.directory()).isEqualTo("dotenv");
        assertThat(cfg.filename()).isEqualTo("smoke.env");
        assertThat(cfg.ignoreIfMissing()).isFalse();
        assertThat(cfg.ignoreIfMalformed()).isTrue();
        assertThat(cfg.systemProperties()).isTrue();
        assertThat(cfg.suppressPrefixDeprecationWarning()).isTrue();
    }
}
