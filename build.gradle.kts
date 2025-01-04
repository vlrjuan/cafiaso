plugins {
    java
    alias(libs.plugins.shadow)
}

group = "org.cafiaso"
version = "1.0-SNAPSHOT"
description = "Cafiaso is a lightweight implementation of a Minecraft server"

repositories {
    mavenCentral()
}

dependencies {
    // Commons cli
    implementation(libs.commons.cli)

    // JSON
    implementation(libs.json)

    // Logging
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.simple)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "org.cafiaso.server.Main"
        }
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveClassifier.set("")
    }
}
