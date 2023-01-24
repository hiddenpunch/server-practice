dependencies {
    implementation(project(":domain"))

    api("com.github.jasync-sql:jasync-postgresql:${Version.jasyncPostgresql}")
    implementation("com.auth0:java-jwt:${Version.javaJwt}")

}