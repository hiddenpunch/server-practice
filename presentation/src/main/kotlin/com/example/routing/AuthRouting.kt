package com.example.routing

import io.ktor.server.routing.Route
import io.ktor.server.routing.post

object AuthRouting {
    fun Route.authRouting() {
        post("/signup") {
            TODO()
        }
        post("/signin") {
            TODO()
        }
    }
}