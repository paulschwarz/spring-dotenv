plugins {
    id("spring-dotenv.java-library")
    id("spring-dotenv.publish")
}

description = "Spring Boot 4 integration for loading .env files into the Spring Environment"

dependencies {
    api(project(":spring-dotenv"))
    compileOnly(platform(libs.spring.boot4.bom))
    compileOnly(libs.spring.boot)
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
            dependencies {
                implementation(platform(libs.spring.boot4.bom))
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
