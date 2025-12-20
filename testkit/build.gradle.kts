plugins {
    id("spring-dotenv.java-library")
    `java-test-fixtures`
}

dependencies {
    testFixturesImplementation(platform(libs.spring6.bom))
    testFixturesImplementation(libs.spring.core)
    testFixturesImplementation(libs.spring.context)
    api(platform(libs.junit.bom))
    api(libs.junit.jupiter.api)
}
