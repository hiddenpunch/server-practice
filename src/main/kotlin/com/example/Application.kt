package com.example

import com.example.repository.UserRepositoryImpl
import com.example.routing.startRestServer
import com.example.service.AuthServiceImpl
import com.example.service.ProductServiceImpl

fun main() {
    val userRepository = UserRepositoryImpl()
    val authService = AuthServiceImpl(userRepository)
    val productService = ProductServiceImpl()
    startRestServer(authService, productService)
}