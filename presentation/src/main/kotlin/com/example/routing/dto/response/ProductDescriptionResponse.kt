package com.example.routing.dto.response

import com.example.routing.dto.Language

data class ProductDescriptionResponse(
    val language: Language,
    val title: String,
    val content: String
)
