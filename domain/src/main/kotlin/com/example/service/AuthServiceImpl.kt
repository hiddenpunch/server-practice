package com.example.service

import arrow.core.Either

class AuthServiceImpl : AuthService {
    override suspend fun signIn(command: AuthService.SignInCommand): Either<AuthService.SignInFailure, AuthService.SignInResult> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(command: AuthService.SignUpCommand): Either<AuthService.SignUpFailure, Unit> {
        TODO("Not yet implemented")
    }
}