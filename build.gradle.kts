plugins {
    java
    alias(libs.plugins.shadow)
    jacoco
}

group = "com.cafiaso"
version = "1.0-SNAPSHOT"
description = "A lightweight implementation of a 1.21.11 Minecraft server without using NMS"

repositories {
    mavenCentral()
}

dependencies {
    // Commons cli
    implementation(libs.commons.cli)

    // Guice
    implementation(libs.guice)
    implementation(libs.guice.assistedinject)

    // Reflections
    implementation(libs.reflections)

    // JSON
    implementation(libs.json)

    // Slf4j
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.simple)

    // JUnit
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    // Mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

sourceSets {
    main {
        java {
            srcDir("src/main/java")
            srcDir("src/generated/java")
        }
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.cafiaso.server.Main"
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier = ""
}

tasks.test {
    useJUnitPlatform()

    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

jacoco {
    toolVersion = "0.8.13"
}
