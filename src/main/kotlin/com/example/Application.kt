package com.example

import com.example.config.DatabaseConfiguration
import com.example.config.TokenConfiguration
import com.example.repository.DatabaseConnection
import com.example.repository.ProductRepositoryImpl
import com.example.repository.UserRepositoryImpl
import com.example.routing.config.JwtConfiguration
import com.example.routing.startRestServer
import com.example.service.AuthServiceImpl
import com.example.service.ProductServiceImpl
import com.example.service.TokenServiceImpl
import com.typesafe.config.ConfigFactory

fun main() {
    val rootConfiguration = ConfigFactory.load()
    val databaseConfiguration = DatabaseConfiguration.load(rootConfiguration)
    val tokenConfiguration = TokenConfiguration.load(rootConfiguration)
    val jwtConfiguration = JwtConfiguration(
        tokenConfiguration.issuer,
        tokenConfiguration.secret
    )
    val connection = DatabaseConnection(databaseConfiguration)
    val tokenService = TokenServiceImpl(tokenConfiguration)

    val userRepository = UserRepositoryImpl(connection)
    val productRepository = ProductRepositoryImpl(connection)
    val authService = AuthServiceImpl(userRepository, tokenService)
    val productService = ProductServiceImpl(productRepository)
    startRestServer(authService, productService, jwtConfiguration)
}