import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.20"
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.refractored"
version = "1.0"

repositories {
    maven ( "https://repo.essentialsx.net/releases/") {
        name = "essentialsx"
    }
    mavenCentral()

    maven ("https://repo.papermc.io/repository/maven-public/")

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }

    maven("https://repo.auxilor.io/repository/maven-public/")

    maven ("https://jitpack.io"){
        name = "jitpack"
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")

    compileOnly("com.willfp:eco:6.70.1")
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
    compileOnly("net.essentialsx:EssentialsX:2.20.1")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}


tasks.withType<ShadowJar>{
    archiveFileName = "ItemPopulator-${version}.jar"
//    reloc('kotlin', 'net.refractored.libs.kotlin')
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
