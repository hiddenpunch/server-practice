package com.example.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.routing.AuthRouting.authRouting
import com.example.routing.ProductDescriptionRouting.productDescriptionRouting
import com.example.routing.ProductRouting.productRouting
import com.example.routing.config.JwtConfiguration
import com.example.service.AuthService
import com.example.service.ProductService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun startRestServer(authService: AuthService, productService: ProductService, jwtConfiguration: JwtConfiguration) {
    embeddedServer(Netty, port = 8080) {
        authentication {
            jwt("auth-jwt") {
                verifier(
                    JWT
                        .require(Algorithm.HMAC256(jwtConfiguration.secret))
                        .withIssuer(jwtConfiguration.issuer)
                        .build()
                )
                validate { credential -> JWTPrincipal(credential.payload) }
            }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    encodeDefaults = true
                    ignoreUnknownKeys = false
                    prettyPrint = true
                    isLenient = true
                    explicitNulls = true
                }
            )
        }
        configureRouting(authService, productService)
    }.start(wait = true)
}

fun Application.configureRouting(authService: AuthService, productService: ProductService) {
    routing {
        authRouting(authService)
        authenticate("auth-jwt") {
            productRouting(productService)
            productDescriptionRouting(productService)
        }
    }
}
