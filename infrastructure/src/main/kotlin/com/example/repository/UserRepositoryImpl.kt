package com.example.repository

import arrow.core.Either
import com.example.entity.User

class UserRepositoryImpl: UserRepository {
    override suspend fun createUser(user: User): Either<UserRepository.CreateFailure, User> {
        TODO("Not yet implemented")
    }

    override suspend fun findUserByEmail(email: String): Either<UserRepository.FindFailure, User?> {
        TODO("Not yet implemented")
    }
}