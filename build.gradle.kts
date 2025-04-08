import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.20"
    java
    id("com.gradleup.shadow") version "8.3.5"
}

group = "net.refractored"
version = "1.0"

repositories {
    maven("https://repo.essentialsx.net/releases/") {
        name = "essentialsx"
    }
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }

    maven("https://repo.auxilor.io/repository/maven-public/")

    maven("https://jitpack.io") {
        name = "jitpack"
    }

    maven("https://repo.nexomc.com/releases") {
        name = "nexo"
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")

    compileOnly(kotlin("stdlib", version = "2.1.0"))

    compileOnly("com.nexomc:nexo:1.1.0")

    compileOnly("com.willfp:eco:6.75.2")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.3-beta-14")

    // Lamp (Commands)
    implementation("com.github.Revxrsal.Lamp:common:3.2.1")
    implementation("com.github.Revxrsal.Lamp:bukkit:3.2.1")

    // Adventure (Components)
    implementation("net.kyori:adventure-platform-bukkit:4.1.2")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")

    // BStats (Metrics)
    implementation("org.bstats:bstats-bukkit:3.0.2")

    // EssentialsX (Economy)
    compileOnly("net.essentialsx:EssentialsX:2.21.0")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.withType<ShadowJar> {
    archiveFileName = "ItemPopulator-$version.jar"
    relocate("kotlin", "net.refractored.libs.kotlin")
    relocate("revxrsal.commands", "net.refractored.libs.lamp")
//    relocate("org.bstats", "net.refractored.libs.bstats")
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
