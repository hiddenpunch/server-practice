package com.example.routing

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route

object ProductRouting {
    private const val PRODUCT_ID_PARAMETER = "product-id"

    fun Route.productRouting() {
        route("/products") {
            post {

            }
            patch("{$PRODUCT_ID_PARAMETER}") {

            }
            post ("{$PRODUCT_ID_PARAMETER}/descriptions") {

            }
            post("{$PRODUCT_ID_PARAMETER}/examine") {

            }
            get("unexamined") {

            }
            get("examined") {

            }
        }
    }
}