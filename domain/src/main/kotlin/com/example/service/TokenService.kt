package com.example.service

import arrow.core.Either
import com.example.entity.User

interface TokenService {
    suspend fun createAccessToken(user: User): Either<CreateAccessTokenFailure, String>
    suspend fun decodeToken(token: String): Either<DecodeTokenFailure, Unit>

    sealed interface CreateAccessTokenFailure {
        data class InternalError(val message: String): CreateAccessTokenFailure
    }
    sealed interface DecodeTokenFailure {
        data class InternalError(val message: String): DecodeTokenFailure
    }
}