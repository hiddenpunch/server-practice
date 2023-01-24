package com.example.config
import com.typesafe.config.Config

data class DatabaseConfiguration(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val database: String,
    val schema: String?
) {
    companion object {
        private const val dbConfigPath = "database"
        fun load(config: Config) =
            DatabaseConfiguration(
                host = config.getString("$dbConfigPath.host"),
                port = config.getInt("$dbConfigPath.port"),
                username = config.getString("$dbConfigPath.username"),
                password = config.getString("$dbConfigPath.password"),
                database = config.getString("$dbConfigPath.database"),
                schema = config.getString("$dbConfigPath.schema")
            )
    }
}