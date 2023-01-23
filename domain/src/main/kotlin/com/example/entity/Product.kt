package com.example.entity

data class Product(
    val id: String,
    val descriptions: List<ProductDescription>,
    val price: Double,
    val commission: Double?,
    val isExamined: Boolean
) {
    data class Update(
        val id: String,
        val price: Double? = null,
        val commission: Double? = null,
        val isExamined: Boolean? = null
    )
}