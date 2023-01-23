package com.example.service

import arrow.core.Either
import com.example.entity.UserRole

interface AuthService {
    suspend fun signIn(command: SignInCommand): Either<SignInFailure, SignInResult>
    suspend fun signUp(command: SignUpCommand): Either<SignUpFailure, Unit>

    data class SignInCommand(
        val email: String,
        val password: String
    )
    data class SignUpCommand(
        val email: String,
        val password: String,
        val role: UserRole
    )

    data class SignInResult(
        val accessToken: String
    )

    sealed interface SignInFailure {
        data class WrongCredential(val message: String): SignInFailure
        data class InternalError(val message: String): SignInFailure
    }
    sealed interface SignUpFailure {
        data class DuplicatedEmail(val email: String): SignUpFailure
        data class InternalError(val message: String): SignUpFailure
    }
}