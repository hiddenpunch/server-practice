package com.example.repository

import arrow.core.Either
import com.example.entity.User

interface UserRepository {
    suspend fun createUser(user: User): Either<CreateFailure, User>
    suspend fun findUserByEmail(email: String): Either<FindFailure, User?>

    sealed interface CreateFailure {
        data class DBError(val message: String): CreateFailure
    }
    sealed interface FindFailure {
        data class DBError(val message: String): FindFailure
    }
}