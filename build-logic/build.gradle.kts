plugins {
    `kotlin-dsl`
}

dependencies {
    // This is the Gradle plugin artifact (not the Error Prone compiler itself)
    implementation(libs.gradle.errorprone.plugin)
}
