package com.example.routing

import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.server.routing.route

object ProductDescriptionRouting {
    private const val PRODUCT_DESCRIPTION_ID_PARAMETER = "product-description-id"
    fun Route.productDescriptionRouting() {
        route("product-descriptions") {
            put("{$PRODUCT_DESCRIPTION_ID_PARAMETER}") {

            }
            delete("{$PRODUCT_DESCRIPTION_ID_PARAMETER}") {

            }
        }
    }
}