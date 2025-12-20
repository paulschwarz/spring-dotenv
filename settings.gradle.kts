pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "spring-dotenv-project"

include(
    "spring-dotenv",
    "springboot3-dotenv",
    "springboot4-dotenv",
    "testkit",
    "spring-dotenv-bom",
)
