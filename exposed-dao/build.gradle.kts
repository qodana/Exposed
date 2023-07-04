plugins {
    kotlin("jvm") apply true
    id("org.jetbrains.kotlinx.kover")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":exposed-core"))
}
