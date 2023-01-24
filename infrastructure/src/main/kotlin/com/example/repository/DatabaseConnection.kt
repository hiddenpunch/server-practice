package com.example.repository

import com.example.config.DatabaseConfiguration
import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.ConnectionPoolConfiguration
import com.github.jasync.sql.db.SuspendingConnection
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory

class DatabaseConnection(config: DatabaseConfiguration) {
    private val connectionPool: ConnectionPool<PostgreSQLConnection>

    init {
        this.connectionPool = ConnectionPool(
            PostgreSQLConnectionFactory(
                Configuration(
                    username = config.username,
                    host = config.host,
                    port = config.port,
                    password = config.password,
                    database = config.database,
                    currentSchema = config.schema
                )
            ),
            ConnectionPoolConfiguration()
        )
    }

    suspend fun sendQuery(query: String) =
        connectionPool.asSuspending.sendQuery(query)
    suspend fun sendPreparedStatement(query: String) =
        connectionPool.asSuspending.sendPreparedStatement(query)
    suspend fun sendPreparedStatement(query: String, values: List<Any?>) =
        connectionPool.asSuspending.sendPreparedStatement(query, values)
    suspend fun <A> inTransaction(f: suspend (SuspendingConnection) -> A): A =
        connectionPool.asSuspending.inTransaction(f)
}