package com.example.entity

data class User(
    val id: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole
)