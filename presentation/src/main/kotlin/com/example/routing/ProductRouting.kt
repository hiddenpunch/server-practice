package com.example.routing

import com.example.routing.dto.UserRole
import com.example.routing.dto.mapper.ProductMapper
import com.example.routing.dto.request.CreateProductDescriptionRequest
import com.example.routing.dto.request.CreateProductRequest
import com.example.routing.dto.request.PatchProductRequest
import com.example.routing.plugin.withRole
import com.example.service.ProductService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.mapstruct.factory.Mappers

object ProductRouting {
    private const val PRODUCT_ID_PARAMETER = "product-id"
    private val mapper = Mappers.getMapper(ProductMapper::class.java)

    fun Route.productRouting(productService: ProductService) {
        route("/products") {
            withRole(UserRole.WRITER) {
                post {
                    val request = call.receive<CreateProductRequest>()
                    productService.createProduct(mapper.toCreateProductCommand(request)).fold(
                        {
                            when (it) {
                                is ProductService.CreateProductFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                            }
                        },
                        {
                            call.respond(HttpStatusCode.Created, mapper.toProductResponse(it))
                        }
                    )
                }
            }
            withRole(UserRole.EDITOR) {
                patch("{$PRODUCT_ID_PARAMETER}") {
                    val productId = call.parameters[PRODUCT_ID_PARAMETER].orEmpty()
                    val request = call.receive<PatchProductRequest>()
                    productService.updateCommission(mapper.toUpdateCommissionCommand(productId, request.commission)).fold(
                        {
                            when (it) {
                                is ProductService.UpdateCommissionFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                            }
                        },
                        {
                            call.respond(HttpStatusCode.OK, mapper.toProductResponse(it))
                        }
                    )
                }
                post ("{$PRODUCT_ID_PARAMETER}/descriptions") {
                    val request = call.receive<CreateProductDescriptionRequest>()
                    val productId = call.parameters[PRODUCT_ID_PARAMETER].orEmpty()
                    productService.createDescription(mapper.toCreateDescriptionCommand(productId, request)).fold(
                        {
                            when (it) {
                                is ProductService.CreateDescriptionFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                            }
                        },
                        {
                            call.respond(HttpStatusCode.Created, mapper.toProductDescriptionResponse(it))
                        }
                    )
                }
                post("{$PRODUCT_ID_PARAMETER}/examine") {
                    val productId = call.parameters[PRODUCT_ID_PARAMETER].orEmpty()
                    productService.examineProduct(productId).fold(
                        {
                            when (it) {
                                is ProductService.ExamineProductFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                            }
                        },
                        {
                            call.respond(HttpStatusCode.OK, mapper.toProductResponse(it))
                        }
                    )
                }
                get("unexamined") {
                    productService.getUnexaminedProducts().fold(
                        {
                            when (it) {
                                is ProductService.GetUnexaminedProductFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                            }
                        },
                        {
                            call.respond(HttpStatusCode.OK, it.map { product -> mapper.toProductResponse(product) })
                        }
                    )
                }
            }
            get("examined") {
                productService.getExaminedProducts().fold(
                    {
                        when (it) {
                            is ProductService.GetExaminedProductFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                        }
                    },
                    {
                        call.respond(HttpStatusCode.OK, it.map { product -> mapper.toProductResponse(product) })
                    }
                )
            }
        }
    }
}