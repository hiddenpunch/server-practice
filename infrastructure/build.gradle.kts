dependencies {
    implementation(project(":domain"))

    api("com.github.jasync-sql:jasync-postgresql:${Version.jasyncPostgresql}")
}