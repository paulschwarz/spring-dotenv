package me.paulschwarz.springdotenv;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import me.paulschwarz.springdotenv.testkit.SystemPropertiesExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

@ResourceLock(Resources.SYSTEM_PROPERTIES)
class DotenvSystemPropertiesPrecedenceTest {

    @RegisterExtension
    static final SystemPropertiesExtension sys = new SystemPropertiesExtension();

    @Test
    void exportToSystemProperties_false_syspropsAreIgnored() {
        sys.set("DOTENV_AND_SYS", "from sysprops");
        sys.set("SYS_ONLY", "from sysprops");

        DotenvPropertySource source = new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "precedence.env",
            "springdotenv.exportToSystemProperties", "false"
        )));

        // sysprops ignored, so dotenv value should win for DOTENV_AND_SYS
        assertThat(source.getProperty("DOTENV_AND_SYS")).isEqualTo("from dotenv");

        // sys-only not visible if sysprops are ignored
        assertThat(source.getProperty("SYS_ONLY")).isNull();
    }

    @Test
    void exportToSystemProperties_true_syspropsAreVisible_andOverrideDotenv() {
        sys.set("DOTENV_AND_SYS", "from sysprops");
        sys.set("SYS_ONLY", "from sysprops");

        DotenvPropertySource source = new DotenvPropertySource(DotenvConfig.load(Map.of(
            "springdotenv.directory", "dotenv",
            "springdotenv.filename", "precedence.env",
            "springdotenv.exportToSystemProperties", "true"
        )));

        // lookup is still dotenv-based
        assertThat(source.getProperty("DOTENV_AND_SYS")).isEqualTo("from dotenv");
        assertThat(source.getProperty("SYS_ONLY")).isNull();

        // but dotenv values are exported into System properties
        assertThat(System.getProperty("DOTENV_AND_SYS")).isEqualTo("from dotenv");
    }
}
