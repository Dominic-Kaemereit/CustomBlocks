plugins {
    id("java")
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
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}