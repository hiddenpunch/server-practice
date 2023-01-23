package com.example.routing

import com.example.routing.dto.mapper.ProductMapper
import com.example.routing.dto.request.UpdateProductDescriptionRequest
import com.example.service.ProductService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.mapstruct.factory.Mappers

object ProductDescriptionRouting {
    private const val PRODUCT_DESCRIPTION_ID_PARAMETER = "product-description-id"
    private val mapper = Mappers.getMapper(ProductMapper::class.java)
    fun Route.productDescriptionRouting(productService: ProductService) {
        route("product-descriptions") {
            put("{$PRODUCT_DESCRIPTION_ID_PARAMETER}") {
                val request = call.receive<UpdateProductDescriptionRequest>()
                productService.updateDescription(mapper.toUpdateDescriptionCommand(request)).fold(
                    {
                        when (it) {
                            is ProductService.UpdateDescriptionFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                        }
                    },
                    {
                        call.respond(HttpStatusCode.OK, mapper.toProductDescriptionResponse(it))
                    }
                )
            }
            delete("{$PRODUCT_DESCRIPTION_ID_PARAMETER}") {
                val descriptionId = call.parameters[PRODUCT_DESCRIPTION_ID_PARAMETER]!!
                productService.deleteDescription(descriptionId).fold(
                    {
                        when (it) {
                            is ProductService.DeleteDescriptionFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                        }
                    },
                    {
                        call.respond(HttpStatusCode.OK)
                    }
                )
            }
        }
    }
}