package com.example.routing.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val id: String,
    val descriptions: List<ProductDescriptionResponse>,
    val price: Double,
    val commission: Double,
    val isExamined: Boolean
)
