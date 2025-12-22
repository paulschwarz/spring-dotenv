plugins {
    `java-library`
    `jvm-test-suite`
    id("net.ltgt.errorprone")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
val javaVersion: Int = libs.findVersion("java").get().requiredVersion.toInt()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    add("errorprone", libs.findLibrary("errorprone-core").get())
}
