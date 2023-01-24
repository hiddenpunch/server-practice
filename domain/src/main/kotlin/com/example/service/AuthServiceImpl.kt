package com.example.service

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.leftIfNull
import com.example.entity.User
import com.example.repository.UserRepository
import com.example.util.HashUtil
import java.util.UUID

class AuthServiceImpl(
    private val repository: UserRepository,
    private val tokenService: TokenService
) : AuthService {
    override suspend fun signIn(command: AuthService.SignInCommand): Either<AuthService.SignInFailure, AuthService.SignInResult> = either {
        val user = repository.findUserByEmail(command.email).mapLeft {
            when (it) {
                is UserRepository.FindFailure.DBError -> AuthService.SignInFailure.InternalError(it.message)
            }
        }.leftIfNull { AuthService.SignInFailure.WrongCredential("No Such Credential") }.bind()

        if (HashUtil.hashSHA256(command.password) == user.passwordHash) {
            val token = tokenService.createAccessToken(user).mapLeft {
                when (it) {
                    is TokenService.CreateAccessTokenFailure.InternalError -> AuthService.SignInFailure.InternalError(it.message)
                }
            }.bind()
            AuthService.SignInResult(token)
        } else {
            Either.Left(AuthService.SignInFailure.WrongCredential("No Such Credential")).bind()
        }
    }

    override suspend fun signUp(command: AuthService.SignUpCommand): Either<AuthService.SignUpFailure, Unit> = either {
        val user = User(
            id = UUID.randomUUID().toString(),
            email = command.email,
            passwordHash = HashUtil.hashSHA256(command.password),
            role = command.role
        )

        repository.createUser(user).mapLeft {
            when (it) {
                is UserRepository.CreateFailure.DBError -> AuthService.SignUpFailure.InternalError(it.message)
            }
        }.bind()
    }
}