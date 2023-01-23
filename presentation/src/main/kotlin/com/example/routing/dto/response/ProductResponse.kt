package com.example.routing.dto.response

data class ProductResponse(
    val id: String,
    val descriptions: List<ProductDescriptionResponse>,
    val price: Double,
    val commission: Double,
    val isExamined: Boolean
)
