package me.paulschwarz.springdotenv.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import me.paulschwarz.springdotenv.DotenvConfig;
import me.paulschwarz.springdotenv.DotenvPropertySource;
import me.paulschwarz.springdotenv.testkit.SystemPropertiesExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

@ResourceLock(Resources.SYSTEM_PROPERTIES)
class SpringDotenvBehaviorTests {

    @RegisterExtension
    static final SystemPropertiesExtension sys = new SystemPropertiesExtension();

    private final SpringDotenvConfigLoader loader = new SpringDotenvConfigLoader();

    @BeforeEach
    void clearSysProps() {
        sys.clear("DOTENV_ONLY");
        sys.clear("DOTENV_AND_SYS");
        sys.clear("SYS_ONLY");
    }

    @Test
    void shouldResolveDotenvOnlyFromDotenv_whenSmokeFilePresent() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvPropertySource source = new DotenvPropertySource(cfg);

        assertThat(source.getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
    }

    @Test
    void shouldNotSupportRelaxedBindingOfEnv() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvPropertySource source = new DotenvPropertySource(cfg);

        assertThat(source.getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
        assertThat(source.getProperty("dotenv.only")).isNull();
        assertThat(source.getProperty("dotenv-only")).isNull();
        assertThat(source.getProperty("dotenv_only")).isNull();
    }

    @Test
    void shouldReturnNull_whenKeyIsMissing() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvPropertySource source = new DotenvPropertySource(cfg);

        assertThat(source.getProperty("MISSING")).isNull();
    }

    @Test
    void shouldReturnNullForAnyKey_whenDotenvFileIsEmpty() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "empty.env"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvPropertySource source = new DotenvPropertySource(cfg);

        assertThat(source.getProperty("DOTENV_ONLY")).isNull();
        assertThat(source.getProperty("SOME_RANDOM_KEY")).isNull();
    }

    @Test
    void shouldNotTreatFileAsMissing_whenDotenvFileIsEmpty_andIgnoreIfMissingFalse() {
        assertThatNoException().isThrownBy(() -> {
            StandardEnvironment env = new StandardEnvironment();
            env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
                "springdotenv.directory", "dotenv",
                "springdotenv.filename", "empty.env",
                "springdotenv.ignoreIfMissing", "false"
            )));

            DotenvConfig cfg = loader.load(env);
            new DotenvPropertySource(cfg);
        });
    }

    @Test
    void shouldNotThrowAndReturnNull_whenDotenvMissing_andIgnoreIfMissingTrue() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "does-not-exist.env",
            "springdotenv.ignoreIfMissing", "true"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvPropertySource source = new DotenvPropertySource(cfg);

        assertThatNoException().isThrownBy(() -> source.getProperty("DOTENV_ONLY"));
        assertThat(source.getProperty("DOTENV_ONLY")).isNull();
    }

    @Test
    void shouldThrow_whenDotenvMissing_andIgnoreIfMissingFalse() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "does-not-exist.env",
            "springdotenv.ignoreIfMissing", "false"
        )));

        DotenvConfig cfg = loader.load(env);

        assertThatThrownBy(() -> new DotenvPropertySource(cfg))
            .hasMessageContaining("Could not find")
            .hasMessageContaining("dotenv/does-not-exist.env");
    }

    @Test
    void shouldSkipBadLinesAndStillLoadGoodOnes_whenIgnoreIfMalformedTrue() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "malformed.env",
            "springdotenv.ignoreIfMalformed", "true"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvPropertySource source = new DotenvPropertySource(cfg);

        assertThat(source.getProperty("GOOD")).isEqualTo("still good");
        assertThat(source.getProperty("ALSO_BAD")).isNull();
    }

    @Test
    void shouldThrowOnMalformedDotenv_whenIgnoreIfMalformedFalse() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "malformed.env",
            "springdotenv.ignoreIfMalformed", "false"
        )));

        DotenvConfig cfg = loader.load(env);

        assertThatThrownBy(() -> new DotenvPropertySource(cfg))
            .message()
            .containsIgnoringCase("malformed entry")
            .contains("THIS_IS_NOT_A_VALID_LINE");
    }

    @Test
    void shouldExportToSystemProperties_whenExportToSystemPropertiesTrue() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.exportToSystemProperties", "true"
        )));

        DotenvConfig cfg = loader.load(env);
        new DotenvPropertySource(cfg);

        assertThat(System.getProperty("DOTENV_ONLY")).isEqualTo("from dotenv");
    }

    @Test
    void shouldNotExportToSystemProperties_whenExportToSystemPropertiesFalse() {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "smoke.env",
            "springdotenv.exportToSystemProperties", "false"
        )));

        DotenvConfig cfg = loader.load(env);
        new DotenvPropertySource(cfg);

        assertThat(System.getProperty("DOTENV_ONLY")).isNull();
    }

    @Test
    void shouldIgnoreSysProps_whenExportToSystemPropertiesFalse() {
        sys.set("DOTENV_AND_SYS", "from sysprops");
        sys.set("SYS_ONLY", "from sysprops");

        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "precedence.env",
            "springdotenv.exportToSystemProperties", "false"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvPropertySource source = new DotenvPropertySource(cfg);

        assertThat(source.getProperty("DOTENV_AND_SYS")).isEqualTo("from dotenv");
        assertThat(source.getProperty("SYS_ONLY")).isNull();
    }

    @Test
    void shouldExportDotenvValuesToSysProps_whenExportToSystemPropertiesTrue() {
        sys.set("DOTENV_AND_SYS", "from sysprops");
        sys.set("SYS_ONLY", "from sysprops");

        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "precedence.env",
            "springdotenv.exportToSystemProperties", "true"
        )));

        DotenvConfig cfg = loader.load(env);
        DotenvPropertySource source = new DotenvPropertySource(cfg);

        // lookup is still dotenv-based
        assertThat(source.getProperty("DOTENV_AND_SYS")).isEqualTo("from dotenv");
        assertThat(source.getProperty("SYS_ONLY")).isNull();

        // export side-effect
        assertThat(System.getProperty("DOTENV_AND_SYS")).isEqualTo("from dotenv");
    }
}
