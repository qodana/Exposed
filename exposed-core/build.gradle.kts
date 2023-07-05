import org.jetbrains.exposed.gradle.Versions

plugins {
    kotlin("jvm") apply true
    kotlin("plugin.serialization") apply true
    id("org.jetbrains.kotlinx.kover")

}

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib"))
    api(kotlin("reflect"))
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Versions.kotlinCoroutines)
    api("org.jetbrains.kotlinx", "kotlinx-serialization-json", Versions.kotlinxSerialization)
    api("org.postgresql", "postgresql", Versions.postgre)
    api("org.slf4j", "slf4j-api", "1.7.25")

    kover(project(":exposed-bom"))
    kover(project(":exposed-crypt"))
    kover(project(":exposed-dao"))
    kover(project(":exposed-jdbc"))
    kover(project(":exposed-jodatime"))
    kover(project(":exposed-money"))
    kover(project(":exposed-spring-boot-starter"))
}
