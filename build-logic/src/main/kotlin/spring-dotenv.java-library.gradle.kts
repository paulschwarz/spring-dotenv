plugins {
    `java-library`
    `jvm-test-suite`
}

val javaVersion = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")
    .findVersion("java")
    .get()
    .requiredVersion
    .toInt()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
    withSourcesJar()
    withJavadocJar()
}
