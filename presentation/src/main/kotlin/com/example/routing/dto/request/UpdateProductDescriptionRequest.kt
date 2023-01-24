package com.example.routing.dto.request

import com.example.routing.dto.Language
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProductDescriptionRequest(
    val language: Language,
    val title: String,
    val content: String
)
