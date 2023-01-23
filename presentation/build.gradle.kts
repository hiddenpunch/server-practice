dependencies {
    implementation("io.ktor:ktor-server-core-jvm:${Version.ktor}")
    implementation("io.ktor:ktor-server-netty-jvm:${Version.ktor}")
    testImplementation("io.ktor:ktor-server-tests-jvm:${Version.ktor}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}")

    implementation(project(":domain"))
}