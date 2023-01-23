package com.example

import com.example.routing.startRestServer
import com.example.service.AuthServiceImpl

fun main() {
    val authService = AuthServiceImpl()
    startRestServer(authService)
}