package me.paulschwarz.springdotenv.springboot;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import me.paulschwarz.springdotenv.DotenvConfig;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;

class Boot4DotenvConfigTests {

    private final Boot4DotenvConfigLoader loader = new Boot4DotenvConfigLoader();

    private DotenvConfig load(Map<String, Object> vars) {
        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(new SystemEnvironmentPropertySource("test", vars));
        return loader.load(env);
    }

    record Case(Map<String, Object> values, Consumer<DotenvConfig> assertions,
                Class<? extends Throwable> expectedExceptionClass) {

        static Case asserting(Map<String, Object> values, Consumer<DotenvConfig> assertions) {
            return new Case(values, assertions, null);
        }

        static Case throwing(Map<String, Object> values) {
            return new Case(values, null, BindException.class);
        }

        @Override
        @NonNull
        public String toString() {
            if (nonNull(expectedExceptionClass)) {
                return "expect " + expectedExceptionClass.getSimpleName() + ": " + values;
            }
            return "case: " + values;
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("bindingSmokeTestCases")
    void shouldBindRelaxedPropertyNames(Case tc) {
        if (nonNull(tc.expectedExceptionClass)) {
            var call = (Runnable) () -> load(tc.values);
            assertThatThrownBy(call::run).isInstanceOf(tc.expectedExceptionClass);
        } else {
            DotenvConfig cfg = load(tc.values);
            tc.assertions.accept(cfg);
        }
    }

    static Stream<Case> bindingSmokeTestCases() {
        return Stream.of(
            // --- ignoreIfMissing: multiple relaxed spellings + partial-config defaults preserved ---
            Case.asserting(Map.of("springdotenv.ignoreIfMissing", "false"), cfg -> {
                assertThat(cfg.ignoreIfMissing()).isFalse();
                assertThat(cfg.exportToSystemProperties()).isFalse(); // default preserved
                assertThat(cfg.filename()).isEqualTo(".env");         // default preserved
            }),
            Case.asserting(Map.of("springdotenv.ignore-if-missing", "false"), cfg ->
                assertThat(cfg.ignoreIfMissing()).isFalse()),
            Case.asserting(Map.of("springdotenv.ignoreifmissing", "false"), cfg ->
                assertThat(cfg.ignoreIfMissing()).isFalse()),
            Case.asserting(Map.of("SPRINGDOTENV_IGNORE_IF_MISSING", "false"), cfg ->
                assertThat(cfg.ignoreIfMissing()).isFalse()),

            // --- exportToSystemProperties: new key + relaxed + legacy alias + precedence ---
            Case.asserting(Map.of("springdotenv.exportToSystemProperties", "true"), cfg ->
                assertThat(cfg.exportToSystemProperties()).isTrue()),
            Case.asserting(Map.of("springdotenv.export-to-system-properties", "true"), cfg ->
                assertThat(cfg.exportToSystemProperties()).isTrue()),
            Case.asserting(Map.of("SPRINGDOTENV_EXPORT_TO_SYSTEM_PROPERTIES", "true"), cfg ->
                assertThat(cfg.exportToSystemProperties()).isTrue()),
            Case.asserting(Map.of("springdotenv.systemProperties", "true"), cfg ->
                assertThat(cfg.exportToSystemProperties()).isTrue()),
            Case.asserting(Map.of("SPRINGDOTENV_SYSTEM_PROPERTIES", "true"), cfg ->
                assertThat(cfg.exportToSystemProperties()).isTrue()),
            Case.asserting(Map.of(
                "springdotenv.systemProperties", "true",
                "springdotenv.exportToSystemProperties", "false"
            ), cfg -> assertThat(cfg.exportToSystemProperties()).isFalse()),

            // --- "some configured, others default" ---
            Case.asserting(Map.of("springdotenv.filename", "custom.env"), cfg -> {
                assertThat(cfg.filename()).isEqualTo("custom.env");
                assertThat(cfg.ignoreIfMissing()).isTrue(); // default preserved
            }),
            Case.asserting(Map.of("springdotenv.enabled", "off"), cfg ->
                assertThat(cfg.enabled()).isFalse()),
            Case.asserting(Map.of("SPRINGDOTENV_ENABLED", "0"), cfg ->
                assertThat(cfg.enabled()).isFalse()),

            // --- invalid values should fail binding ---
            Case.throwing(Map.of("springdotenv.ignoreIfMissing", "maybe")),
            Case.throwing(Map.of("SPRINGDOTENV_IGNORE_IF_MISSING", "maybe")),
            Case.throwing(Map.of("springdotenv.exportToSystemProperties", "maybe")),
            Case.throwing(Map.of("springdotenv.systemProperties", "maybe")),
            Case.throwing(Map.of("SPRINGDOTENV_SYSTEM_PROPERTIES", "maybe"))
        );
    }

    @Test
    void shouldUseDefaults_whenNoPropertiesAreProvided() {
        DotenvConfig cfg = loader.load(new StandardEnvironment());

        assertThat(cfg.enabled()).isTrue();
        assertThat(cfg.filename()).isEqualTo(".env");
        assertThat(cfg.directory()).isNull();
        assertThat(cfg.ignoreIfMissing()).isTrue();
        assertThat(cfg.ignoreIfMalformed()).isFalse();
        assertThat(cfg.exportToSystemProperties()).isFalse();
    }
}
