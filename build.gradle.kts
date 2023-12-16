plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.d151l.custom.block"
version = "1.2.2-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    compileOnly("commons-io:commons-io:2.7")

    compileOnly("com.jeff-media:custom-block-data:2.2.2")
    implementation("dev.triumphteam:triumph-gui:3.1.7")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}