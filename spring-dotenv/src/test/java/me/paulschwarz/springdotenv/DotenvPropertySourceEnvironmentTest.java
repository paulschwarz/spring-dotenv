//package me.paulschwarz.springdotenv;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.Properties;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//class DotenvPropertySourceEnvironmentTest {
//
//    private DotenvPropertySource source;
//
//    @BeforeEach
//    void init() {
//        source = new DotenvPropertySource(new DotenvConfig(new Properties()));
//    }
//
//    @Test
//    void irrelevant() {
//        assertThat(source.getProperty("other.VALUE")).isNull();
//    }
//
//    @Test
//    void missing() {
//        assertThat(source.getProperty("MISSING")).isNull();
//    }
//
//    @Test
//    void valueFromDotenv() {
//        assertThat(source.getProperty("EXAMPLE_MESSAGE_1")).isEqualTo("Message 1 from .env");
//    }
//
//    @Test
//    void givenPrefixEnv_valueIsNull() {
//        assertThat(source.getProperty("env.EXAMPLE_MESSAGE_1")).isNull();
//    }
//
//    @Test
//    void valueFromEnvironment() {
//        assertThat(source.getProperty("EXAMPLE_MESSAGE_2"))
//            .asString()
//            .contains("Message 2 from system environment")
//            .contains("WINNER");
//    }
//
//    @Test
//    void valueFromDotEnv() {
//        assertThat(source.getProperty("EXAMPLE_MESSAGE_3"))
//            .asString()
//            .contains("Message 3 from .env");
//    }
//}
