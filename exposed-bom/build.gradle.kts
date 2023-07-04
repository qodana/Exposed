import org.jetbrains.exposed.gradle.configureMavenCentralMetadata
import org.jetbrains.exposed.gradle.signPublicationIfKeyPresent

plugins {
    `java-platform`
    `maven-publish`
    signing
    id("org.jetbrains.kotlinx.kover")
}

group = "org.jetbrains.exposed"

// This is needed as the api dependency constraints cause dependencies
javaPlatform.allowDependencies()

dependencies {
    constraints {
        rootProject.subprojects.forEach {
            if (it.plugins.hasPlugin("maven-publish") && it.name != name) {
                it.publishing.publications.all {
                    if (this is MavenPublication) {
                        if (!artifactId.endsWith("-metadata") &&
                            !artifactId.endsWith("-kotlinMultiplatform")
                        ) {
                            api(project(":${it.name}"))
                        }
                    }
                }
            }
        }
    }
    kover(project(":exposed-core"))
    kover(project(":exposed-crypt"))
    kover(project(":exposed-dao"))
    kover(project(":exposed-java-time"))
    kover(project(":exposed-jdbc"))
    kover(project(":exposed-jodatime"))
    kover(project(":exposed-kotlin-datetime"))
    kover(project(":exposed-money"))
    kover(project(":exposed-spring-boot-starter"))
    kover(project(":exposed-tests"))
}

publishing {
    publications {
        create<MavenPublication>("bom") {
            from(components.getByName("javaPlatform"))
            pom {
                configureMavenCentralMetadata(project)
            }
            signPublicationIfKeyPresent(project)
        }
    }
}
repositories {
    mavenLocal()
    mavenCentral()
}
