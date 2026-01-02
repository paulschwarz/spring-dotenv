package me.paulschwarz.springdotenv.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import me.paulschwarz.springdotenv.DotenvConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

class SpringDotenvBindingTests {

    private final SpringDotenvConfigLoader loader = new SpringDotenvConfigLoader();

    private DotenvConfig loadFrom(Map<String, Object> props) {
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource("test", props));
        return loader.load(env);
    }

    @Test
    void shouldUseDefaults_whenNoPropertiesPresent() {
        DotenvConfig cfg = loadFrom(Map.of());
        DotenvConfig d = DotenvConfig.defaults();

        assertThat(cfg.enabled()).isEqualTo(d.enabled());
        assertThat(cfg.directory()).isEqualTo(d.directory());
        assertThat(cfg.filename()).isEqualTo(d.filename());
        assertThat(cfg.ignoreIfMalformed()).isEqualTo(d.ignoreIfMalformed());
        assertThat(cfg.ignoreIfMissing()).isEqualTo(d.ignoreIfMissing());
        assertThat(cfg.exportToSystemProperties()).isEqualTo(d.exportToSystemProperties());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("dotKeyCases")
    void shouldBindConfiguration_whenDotKeysAreUsed(String _name, Map<String, Object> props, DotenvConfig expected) {
        DotenvConfig cfg = loadFrom(props);

        assertThat(cfg.enabled()).isEqualTo(expected.enabled());
        assertThat(cfg.directory()).isEqualTo(expected.directory());
        assertThat(cfg.filename()).isEqualTo(expected.filename());
        assertThat(cfg.ignoreIfMalformed()).isEqualTo(expected.ignoreIfMalformed());
        assertThat(cfg.ignoreIfMissing()).isEqualTo(expected.ignoreIfMissing());
        assertThat(cfg.exportToSystemProperties()).isEqualTo(expected.exportToSystemProperties());
    }

    static Stream<Arguments> dotKeyCases() {
        DotenvConfig d = DotenvConfig.defaults();

        return Stream.of(
            Arguments.of(
                "enabled_binds_from_spring_property",
                Map.of("springdotenv.enabled", "false"),
                new DotenvConfig(false, d.directory(), d.filename(), d.ignoreIfMalformed(), d.ignoreIfMissing(),
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "directory_binds_from_spring_property",
                Map.of("springdotenv.directory", "dotenv"),
                new DotenvConfig(d.enabled(), "dotenv", d.filename(), d.ignoreIfMalformed(), d.ignoreIfMissing(),
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "filename_binds_from_spring_property",
                Map.of("springdotenv.filename", "x.env"),
                new DotenvConfig(d.enabled(), d.directory(), "x.env", d.ignoreIfMalformed(), d.ignoreIfMissing(),
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "ignoreIfMalformed_binds_from_spring_property",
                Map.of("springdotenv.ignoreIfMalformed", "true"),
                new DotenvConfig(d.enabled(), d.directory(), d.filename(), true, d.ignoreIfMissing(),
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "ignoreIfMissing_binds_from_spring_property",
                Map.of("springdotenv.ignoreIfMissing", "false"),
                new DotenvConfig(d.enabled(), d.directory(), d.filename(), d.ignoreIfMalformed(), false,
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "exportToSystemProperties_binds_from_spring_property",
                Map.of("springdotenv.exportToSystemProperties", "true"),
                new DotenvConfig(d.enabled(), d.directory(), d.filename(), d.ignoreIfMalformed(), d.ignoreIfMissing(),
                    true)
            )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("envVarCases")
    void shouldBindConfiguration_whenEnvVarKeysAreUsed(String _name, Map<String, Object> props, DotenvConfig expected) {
        DotenvConfig cfg = loadFrom(props);

        assertThat(cfg.enabled()).isEqualTo(expected.enabled());
        assertThat(cfg.directory()).isEqualTo(expected.directory());
        assertThat(cfg.filename()).isEqualTo(expected.filename());
        assertThat(cfg.ignoreIfMalformed()).isEqualTo(expected.ignoreIfMalformed());
        assertThat(cfg.ignoreIfMissing()).isEqualTo(expected.ignoreIfMissing());
        assertThat(cfg.exportToSystemProperties()).isEqualTo(expected.exportToSystemProperties());
    }

    static Stream<Arguments> envVarCases() {
        DotenvConfig d = DotenvConfig.defaults();

        return Stream.of(
            Arguments.of(
                "enabled_binds_from_env_var",
                Map.of("SPRINGDOTENV_ENABLED", "false"),
                new DotenvConfig(false, d.directory(), d.filename(), d.ignoreIfMalformed(), d.ignoreIfMissing(),
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "directory_binds_from_env_var",
                Map.of("SPRINGDOTENV_DIRECTORY", "dotenv"),
                new DotenvConfig(d.enabled(), "dotenv", d.filename(), d.ignoreIfMalformed(), d.ignoreIfMissing(),
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "filename_binds_from_env_var",
                Map.of("SPRINGDOTENV_FILENAME", "x.env"),
                new DotenvConfig(d.enabled(), d.directory(), "x.env", d.ignoreIfMalformed(), d.ignoreIfMissing(),
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "ignoreIfMalformed_binds_from_env_var",
                Map.of("SPRINGDOTENV_IGNORE_IF_MALFORMED", "true"),
                new DotenvConfig(d.enabled(), d.directory(), d.filename(), true, d.ignoreIfMissing(),
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "ignoreIfMissing_binds_from_env_var",
                Map.of("SPRINGDOTENV_IGNORE_IF_MISSING", "false"),
                new DotenvConfig(d.enabled(), d.directory(), d.filename(), d.ignoreIfMalformed(), false,
                    d.exportToSystemProperties())
            ),
            Arguments.of(
                "exportToSystemProperties_binds_from_env_var",
                Map.of("SPRINGDOTENV_EXPORT_TO_SYSTEM_PROPERTIES", "true"),
                new DotenvConfig(d.enabled(), d.directory(), d.filename(), d.ignoreIfMalformed(), d.ignoreIfMissing(),
                    true)
            )
        );
    }

    @Test
    void shouldPreferDotKeyOverEnvVar_whenBothArePresent() {
        DotenvConfig cfg = loadFrom(Map.of(
            "springdotenv.enabled", "false",
            "SPRINGDOTENV_ENABLED", "true"
        ));

        assertThat(cfg.enabled()).isFalse();
    }

    @Test
    void shouldPreferNewExportToSystemPropertiesOverLegacySystemProperties_whenBothArePresent() {
        Map<String, Object> props = new LinkedHashMap<>();
        props.put("springdotenv.systemProperties", "true"); // legacy
        props.put("springdotenv.exportToSystemProperties", "false"); // new

        DotenvConfig cfg = loadFrom(props);

        assertThat(cfg.exportToSystemProperties()).isFalse();
    }

    @Test
    void shouldBindLegacySystemProperties_whenNewKeyIsAbsent() {
        DotenvConfig cfg = loadFrom(Map.of(
            "springdotenv.systemProperties", "true"
        ));

        assertThat(cfg.exportToSystemProperties()).isTrue();
    }

    @Test
    void shouldNotBindOverlyGenerousVariants_whenTheyArePresent() {
        DotenvConfig defaults = DotenvConfig.defaults();

        DotenvConfig cfg = loadFrom(Map.of(
            "springdotenv.fileName", "nope.env",
            "springdotenv.ignoreifmissing", "false",
            "springdotenv.export-to-system-properties", "true",
            "SPRINGDOTENV_IGNOREIFMISSING", "false"
        ));

        assertThat(cfg.filename()).isEqualTo(defaults.filename());
        assertThat(cfg.ignoreIfMissing()).isEqualTo(defaults.ignoreIfMissing());
        assertThat(cfg.exportToSystemProperties()).isEqualTo(defaults.exportToSystemProperties());
    }

    @Test
    void shouldTrimBooleanValues_whenParsing() {
        DotenvConfig cfg = loadFrom(Map.of(
            "springdotenv.enabled", "  false  ",
            "SPRINGDOTENV_IGNORE_IF_MISSING", "\ttrue\n"
        ));

        assertThat(cfg.enabled()).isFalse();
        assertThat(cfg.ignoreIfMissing()).isTrue();
    }
}
