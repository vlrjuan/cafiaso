import groovy.json.JsonSlurper
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    java
}

group = "org.cafiaso"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JavaPoet
    implementation(libs.javapoet)

    // JSON
    implementation(libs.json)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

val manifestUrl = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"

val serverJarProvider: Provider<RegularFile> = layout.buildDirectory.file("server.jar")
val serverJarFile: File = serverJarProvider.get().asFile

tasks.register("downloadServerJar") {
    group = "data"
    description = "Downloads the Minecraft server jar"

    outputs.file(serverJarFile)

    doLast {
        serverJarFile.parentFile.mkdirs()

        // Download and parse the manifest JSON
        val manifestText = URI.create(manifestUrl).toURL().readText()
        val manifest = JsonSlurper().parseText(manifestText) as Map<*, *>

        // Get the latest release version ID
        val latestReleaseId = (manifest["latest"] as Map<*, *>)["release"] as String

        // Find the version entry for the latest release
        @Suppress("UNCHECKED_CAST")
        val versions = manifest["versions"] as List<Map<*, *>>
        val versionEntry = versions.first { it["id"] == latestReleaseId }
        val versionUrl = versionEntry["url"] as String

        // Download and parse the version JSON
        val versionText = URI.create(versionUrl).toURL().readText()
        val versionJson = JsonSlurper().parseText(versionText) as Map<*, *>
        val downloads = versionJson["downloads"] as Map<*, *>
        val serverEntry = downloads["server"] as Map<*, *>
        val serverUrl = serverEntry["url"] as String

        // Download the server jar
        Files.copy(
            URI.create(serverUrl).toURL().openStream(),
            serverJarFile.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }
}

tasks.register<Exec>("runDataGenerator") {
    group = "data"
    description = "Executes the Minecraft data generator"

    workingDir = serverJarFile.parentFile

    commandLine(
        "java",
        "-DbundlerMainClass=net.minecraft.data.Main",
        "-jar",
        serverJarFile.absolutePath,
        "--all"
    )
}

tasks.register<Copy>("copyServerJar") {
    group = "data"
    description = "Copies the downloaded server jar to the resources directory"

    val targetDir = layout.projectDirectory.dir("src/main/resources")

    from(serverJarFile)
    into(targetDir)
}

tasks.register<Copy>("copyReports") {
    group = "data"
    description = "Copies generated report files to the resources directory"

    val generatedReportsDir = layout.buildDirectory.dir("generated/reports")
    val targetDir = layout.projectDirectory.dir("src/main/resources/reports")

    from(generatedReportsDir) {
        include("packets.json")
    }
    into(targetDir)
}
