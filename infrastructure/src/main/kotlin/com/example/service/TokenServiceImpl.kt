package com.example.service

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.right
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.TokenConfiguration
import com.example.entity.User
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class TokenServiceImpl(
    private val tokenConfiguration: TokenConfiguration,
): TokenService {
    private val header = mapOf("typ" to "JWT", "alg" to "HMAC256")
    private val issuer = tokenConfiguration.issuer
    private val accessExpiry = tokenConfiguration.accessExpiry
    private val secret = tokenConfiguration.secret

    override suspend fun createAccessToken(user: User): Either<TokenService.CreateAccessTokenFailure, String> = either {
        try {
            val now = LocalDateTime.now()
            val expiredAt = now.plusSeconds(accessExpiry)
            val jwtBuilder = JWT.create()

            jwtBuilder
                .withHeader(header)
                .withClaim("id", user.id)
                .withClaim("role", user.role.name)
                .withIssuer(issuer)
                .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .withExpiresAt(Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(
                    Algorithm.HMAC256(
                        secret
                    )
                )
        } catch (e: Exception) {
            Either.Left(TokenService.CreateAccessTokenFailure.InternalError(e.message.orEmpty())).bind()
        }
    }

    override suspend fun decodeToken(token: String): Either<TokenService.DecodeTokenFailure, Unit> = either {
        try {
            JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
            Unit.right()
        } catch (e: Exception) {
            TokenService.DecodeTokenFailure.InternalError(e.message.orEmpty())
        }
    }
}