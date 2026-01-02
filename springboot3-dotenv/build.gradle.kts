plugins {
    id("spring-dotenv.java-library")
    id("spring-dotenv.publish")
}

description = "Spring Boot 3 integration for loading .env files into the Spring Environment"

dependencies {
    api(project(":spring-dotenv"))
    compileOnly(platform(libs.spring.boot3.bom))
    compileOnly(libs.spring.boot)
    annotationProcessor(platform(libs.spring.boot3.bom))
    annotationProcessor(libs.spring.boot.configuration.processor)
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
            dependencies {
                implementation(platform(libs.spring.boot3.bom))
                implementation(libs.spring.boot)
                implementation(libs.assertj)
                implementation(testFixtures(project(":testkit")))
            }

            targets {
                all {
                    tasks.test {
                        environment("DOTENV_AND_ENV", "from env")
                    }
                }
            }
        }
    }
}
