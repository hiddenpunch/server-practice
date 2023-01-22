val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.2"
    application
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.gradle.java-test-fixtures")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
}

tasks.getByName<Jar>("jar") {
    enabled = true

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes("Main-Class" to "finance.chai.ApplicationKt")
    }

    from(
        configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) }
    )

    val sourcesMain = sourceSets.main.get()
    from(sourcesMain.output)
}