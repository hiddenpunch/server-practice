package com.example.repository

import RepositoryUtil
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.entity.User
import java.lang.Exception

class UserRepositoryImpl(
    private val connection: DatabaseConnection
): UserRepository {
    override suspend fun createUser(user: User): Either<UserRepository.CreateFailure, User> {
        val createQuery = """
            INSERT INTO users (id, email, password_hash, role) VALUES (?, ?, ?, ?)
            RETURNING id, email, password_hash, role;
        """.trimIndent()

        return try {
            val result = connection.sendPreparedStatement(
                createQuery,
                listOf(user.id, user.email, user.passwordHash, user.role)
            )
            RepositoryUtil.queryResultMapper(result, User::class)!!.right()
        } catch (e: Exception) {
            UserRepository.CreateFailure.DBError(e.message.orEmpty()).left()
        }
    }

    override suspend fun findUserByEmail(email: String): Either<UserRepository.FindFailure, User?> {
        val findQuery = "SELECT id, email, password_hash, role FROM users where email = ?;"
        return try {
            val result = connection.sendPreparedStatement(
                findQuery,
                listOf(email)
            )
            RepositoryUtil.queryResultMapper(result, User::class).right()
        } catch (e: Exception) {
            UserRepository.FindFailure.DBError(e.message.orEmpty()).left()
        }
    }
}