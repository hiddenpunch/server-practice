package com.example.routing.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class PatchProductRequest(
    val commission: Double
)
