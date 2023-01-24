package com.example.routing.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val accessToken: String
)
