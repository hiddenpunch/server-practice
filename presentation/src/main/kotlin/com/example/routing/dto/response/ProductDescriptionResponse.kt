package com.example.routing.dto.response

import com.example.routing.dto.Language
import kotlinx.serialization.Serializable

@Serializable
data class ProductDescriptionResponse(
    val id: String,
    val language: Language,
    val title: String,
    val content: String
)
