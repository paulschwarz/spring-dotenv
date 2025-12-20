import org.gradle.api.publish.maven.MavenPublication

plugins {
    `maven-publish`
    signing
}

// Allow CI to set version via ORG_GRADLE_PROJECT_releaseVersion
providers.gradleProperty("releaseVersion").orNull?.let { v ->
    if (v.isNotBlank()) version = v
}

fun pomFor(pub: MavenPublication) {
    pub.pom {
        name.set(project.name)
        description.set(project.description ?: project.name)
        url.set("https://github.com/paulschwarz/spring-dotenv")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://raw.githubusercontent.com/paulschwarz/spring-dotenv/master/LICENSE")
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
            developerConnection.set("scm:git:git@github.com:paulschwarz/spring-dotenv.git")
            url.set("https://github.com/paulschwarz/spring-dotenv")
        }
    }
}

publishing {
    publications {
        // If the project is a normal Java library, publish 'java'
        plugins.withId("java") {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                pomFor(this)
            }
        }

        // If the project is a BOM (java-platform), publish 'javaPlatform'
        plugins.withId("java-platform") {
            create<MavenPublication>("mavenPlatform") {
                from(components["javaPlatform"])
                pomFor(this)
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"

            credentials {
                username = providers.gradleProperty("ossrhUsername").orNull
                password = providers.gradleProperty("ossrhPassword").orNull
            }

            val repoBaseUrl = "https://oss.sonatype.org"
            val releasesRepoUrl = "$repoBaseUrl/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "$repoBaseUrl/content/repositories/snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }
}

// Configure signing *after* publications exist
afterEvaluate {
    val signingKey = providers.gradleProperty("signingKey").orNull?.replace("\\n", "\n")
    val signingPassword = providers.gradleProperty("signingPassword").orNull

    if (!signingKey.isNullOrBlank()) {
        signing {
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications)
        }
    }
}
