package com.example.routing

import com.example.routing.AuthRouting.authRouting
import com.example.routing.ProductDescriptionRouting.productDescriptionRouting
import com.example.routing.ProductRouting.productRouting
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun startRestServer() {
    embeddedServer(Netty, port = 8080){
        configureRouting()
    }.start(wait = true)
}
fun Application.configureRouting() {
    routing {
        authRouting()
        productRouting()
        productDescriptionRouting()
    }
}
