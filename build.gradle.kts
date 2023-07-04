import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") apply true
    id("io.github.gradle-nexus.publish-plugin") apply true
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.2"
    id("org.jetbrains.kotlinx.kover") version "0.7.2"
}

allprojects {
    apply(from = rootProject.file("buildScripts/gradle/checkstyle.gradle.kts"))
    if (this.name != "exposed-tests" && this.name != "exposed-bom" && this != rootProject) {
        apply(from = rootProject.file("buildScripts/gradle/publishing.gradle.kts"))
    }
}

//dependencies {
//    val subProjectsForKover = setOf("spring-transaction")
//    subprojects
//        .filter { !subProjectsForKover.contains(it.name) }
//        .forEach { kover(project(":${it.name}")) }
//    subprojects.forEach { kover(project(":${it.name}")) }
//    kover(project(":exposed-bom"))
//    kover(project(":exposed-core"))
//    kover(project(":exposed-crypt"))
//    kover(project(":exposed-dao"))
//    kover(project(":exposed-java-time"))
//    kover(project(":exposed-jdbc"))
//    kover(project(":exposed-jodatime"))
//    kover(project(":exposed-kotlin-datetime"))
//    kover(project(":exposed-money"))
//
    // }
//koverReport {
//    defaults {
//        xml {
//            onCheck = true
//        }
//    }
//}
apiValidation {
    ignoredProjects.addAll(listOf("exposed-tests", "exposed-bom"))
}

val reportMerge by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.buildDir.resolve("reports/detekt/exposed.xml"))
}

subprojects {
    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt", "detekt-formatting", "1.21.0")
    }
    tasks.withType<Detekt>().configureEach detekt@{
        enabled = this@subprojects.name !== "exposed-tests"
        finalizedBy(reportMerge)
        reportMerge.configure {
            input.from(this@detekt.xmlReportFile)
        }
    }
    tasks.withType<KotlinJvmCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            apiVersion = "1.6"
            languageVersion = "1.6"
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}
