package com.example

import com.example.config.DatabaseConfiguration
import com.example.repository.DatabaseConnection
import com.example.repository.ProductRepositoryImpl
import com.example.repository.UserRepositoryImpl
import com.example.routing.startRestServer
import com.example.service.AuthServiceImpl
import com.example.service.ProductServiceImpl
import com.typesafe.config.ConfigFactory

fun main() {
    val rootConfiguration = ConfigFactory.load()
    val databaseConfiguration = DatabaseConfiguration.load(rootConfiguration)
    val connection = DatabaseConnection(databaseConfiguration)

    val userRepository = UserRepositoryImpl(connection)
    val productRepository = ProductRepositoryImpl(connection)
    val authService = AuthServiceImpl(userRepository)
    val productService = ProductServiceImpl(productRepository)
    startRestServer(authService, productService)
}