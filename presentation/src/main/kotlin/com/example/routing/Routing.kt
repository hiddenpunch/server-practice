package com.example.routing

import com.example.routing.AuthRouting.authRouting
import com.example.routing.ProductDescriptionRouting.productDescriptionRouting
import com.example.routing.ProductRouting.productRouting
import com.example.service.AuthService
import com.example.service.ProductService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun startRestServer(authService: AuthService, productService: ProductService) {
    embeddedServer(Netty, port = 8080){
        configureRouting(authService, productService)
    }.start(wait = true)
}
fun Application.configureRouting(authService: AuthService, productService: ProductService) {
    routing {
        authRouting(authService)
        productRouting(productService)
        productDescriptionRouting(productService)
    }
}
