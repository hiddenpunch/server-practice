val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version Version.kotlin
    kotlin("kapt") version Version.kapt
    kotlin("plugin.serialization") version Version.kotlin
    application
}

group = "com.example"
version = Version.project
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
    apply(plugin = "kotlin-kapt")
    apply(plugin = "kotlinx-serialization")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin}")
        implementation("io.arrow-kt:arrow-core:${Version.arrowCore}")
        implementation("org.mapstruct:mapstruct:${Version.mapStruct}")
        kapt("org.mapstruct:mapstruct-processor:${Version.mapStruct}")
        implementation("com.typesafe:config:${Version.typesafeConfig}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinxSerialization}")
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    implementation("ch.qos.logback:logback-classic:$logback_version")
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