plugins {
    `java-platform`
    id("spring-dotenv.publish")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        api(project(":spring-dotenv"))
        api(project(":springboot3-dotenv"))
        api(project(":springboot4-dotenv"))
    }
}
