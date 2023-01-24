dependencies {
    implementation("io.ktor:ktor-server-core-jvm:${Version.ktor}")
    implementation("io.ktor:ktor-server-netty-jvm:${Version.ktor}")
    implementation("io.ktor:ktor-server-content-negotiation:${Version.ktor}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${Version.ktor}")

    testImplementation("io.ktor:ktor-server-tests-jvm:${Version.ktor}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}")

    implementation(project(":domain"))
}