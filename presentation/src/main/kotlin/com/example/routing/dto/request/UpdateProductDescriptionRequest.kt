package com.example.routing.dto.request

import com.example.routing.dto.Language

data class UpdateProductDescriptionRequest(
    val lang: Language,
    val title: String,
    val content: String
)
