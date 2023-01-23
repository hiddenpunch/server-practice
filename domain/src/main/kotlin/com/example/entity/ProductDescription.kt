package com.example.entity

data class ProductDescription(
    val id: String,
    val productId: String,
    val language: Language,
    val title: String,
    val content: String
) {
    data class Update(
        val id: String,
        val language: Language,
        val title: String,
        val content: String
    )
}
