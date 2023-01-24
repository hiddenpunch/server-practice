package com.example.routing.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateProductRequest(
    val title: String,
    val content: String,
    val price: Double
)
