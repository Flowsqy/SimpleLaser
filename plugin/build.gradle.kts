plugins {
    `java-library`
    plugins {
        id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.annotations)
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

project.base.archivesName.set(rootProject.name)
group = "fr.flowsqy.configdeathmessage"
version = "1.2.0-SNAPSHOT"

tasks.processResources {
    expand(Pair("version", version))
}

