package com.example.routing.dto.request

import com.example.routing.dto.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    val password: String,
    val role: UserRole
)
