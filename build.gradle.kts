plugins {
    base
}

allprojects {
    group = "me.paulschwarz"
    version = findProperty("releaseVersion")?.toString() ?: "0.0.0-SNAPSHOT"
}
