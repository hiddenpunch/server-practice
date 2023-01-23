package com.example

import com.example.routing.startRestServer
import com.example.service.AuthServiceImpl
import com.example.service.ProductServiceImpl

fun main() {
    val authService = AuthServiceImpl()
    val productService = ProductServiceImpl()
    startRestServer(authService, productService)
}