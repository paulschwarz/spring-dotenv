plugins {
    `maven-publish`
    signing
}

// --- Version injection (CI-controlled) ---
val releaseVersion = providers.gradleProperty("releaseVersion").orNull?.trim().orEmpty()
val snapshotVersion = providers.gradleProperty("snapshotVersion").orNull?.trim().orEmpty()

when {
    releaseVersion.isNotBlank() -> version = releaseVersion
    snapshotVersion.isNotBlank() -> version = snapshotVersion
    // fallback (local dev): keep whatever is already set in the project, or default:
    version.toString().isBlank() -> version = "0.0.0-SNAPSHOT"
}

// --- POM metadata ---
fun MavenPublication.configurePom() {
    pom {
        name.set(project.name)
        description.set(project.description ?: project.name)
        url.set("https://github.com/paulschwarz/spring-dotenv")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://raw.githubusercontent.com/paulschwarz/spring-dotenv/main/LICENSE")
            }
        }
        developers {
            developer {
                id.set("paulschwarz")
                name.set("Paul Schwarz")
                email.set("paul@paulschwarz.me")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/paulschwarz/spring-dotenv.git")
            developerConnection.set("scm:git:ssh://git@github.com/paulschwarz/spring-dotenv.git")
            url.set("https://github.com/paulschwarz/spring-dotenv")
        }
    }
}

publishing {
    publications {
        // Publish java components when present
        plugins.withId("java") {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                configurePom()
            }
        }

        // Publish BOM when present
        plugins.withId("java-platform") {
            create<MavenPublication>("mavenPlatform") {
                from(components["javaPlatform"])
                configurePom()
            }
        }
    }

    repositories {
        // Sonatype Central Portal compatibility endpoints (OSSRH Staging API)
        maven {
            name = "MavenCentralPortal"

            val releasesRepoUrl =
                "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl =
                "https://central.sonatype.com/repository/maven-snapshots/"

            url = uri(
                if (version.toString().endsWith("-SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            )

            credentials {
                // These are CENTRAL PORTAL USER TOKEN username/password
                username = providers.gradleProperty("centralTokenUsername").orNull
                password = providers.gradleProperty("centralTokenPassword").orNull
            }
        }
    }
}

// --- Signing (lazy + CI-friendly) ---
val signingKey = providers.gradleProperty("signingKey").orNull?.replace("\\n", "\n")
val signingPassphrase = providers.gradleProperty("signingPassphrase").orNull

// Only require signing when publishing (keeps local dev friction low)
tasks.withType<PublishToMavenRepository>().configureEach {
    onlyIf { !signingKey.isNullOrBlank() }
}

extensions.configure<SigningExtension>("signing") {
    if (!signingKey.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKey, signingPassphrase)
        sign(publishing.publications)
    }
}
