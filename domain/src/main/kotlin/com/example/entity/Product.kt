package com.example.entity

data class Product(
    val id: String,
    val descriptions: List<ProductDescription>,
    val price: Double,
    val commission: Double?,
    val isExamined: Boolean
)