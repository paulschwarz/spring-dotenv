plugins {
    id("spring-dotenv.java-library")
    id("spring-dotenv.publish")
}

description = "Core library for loading .env files and resolving environment variables"

dependencies {
    implementation(libs.dotenv)

    compileOnly(platform(libs.spring6.bom))
    compileOnly(libs.spring.core)
    compileOnly(libs.spring.context)
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
            dependencies {
                implementation(enforcedPlatform(libs.spring6.bom))
                implementation(libs.spring.core)
                implementation(libs.assertj)
                implementation(libs.mockito)
                implementation(testFixtures(project(":testkit")))
            }
        }

        register<JvmTestSuite>("integrationTest") {
            useJUnitJupiter()
            dependencies {
                implementation(enforcedPlatform(libs.spring6.bom))
                implementation(libs.spring.core)
                implementation(libs.spring.context)
                implementation(libs.assertj)
                implementation(project())
                implementation(testFixtures(project(":testkit")))
            }
        }
    }
}

tasks.named("check") {
    dependsOn(tasks.named("integrationTest"))
}

tasks.named("integrationTest") {
    shouldRunAfter(tasks.named("test"))
}
