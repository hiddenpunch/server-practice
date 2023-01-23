package com.example.routing.dto.request

data class CreateProductRequest(
    val title: String,
    val content: String,
    val price: Double
)
