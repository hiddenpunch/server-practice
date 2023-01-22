package com.example.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*
import io.ktor.server.response.*
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
        route("/") {
            get {
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
