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

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
    // Commons cli
    implementation(libs.commons.cli)

    // JSON
    implementation(libs.json)

    // Logging
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.simple)

    // JUnit
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    // Mockito
    testImplementation(libs.mockito.core)
    mockitoAgent(libs.mockito.core) {
        isTransitive = false
    }
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

    test {
        useJUnitPlatform()
        jvmArgs("-javaagent:${mockitoAgent.asPath}", "-XX:+EnableDynamicAgentLoading", "-Xshare:off")
    }
}

