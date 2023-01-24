package com.example.config

import com.typesafe.config.Config
import java.security.PublicKey

data class TokenConfiguration(val issuer: String, val secret: String, val accessExpiry: Long) {
    companion object {
        fun load(config: Config): TokenConfiguration =
            TokenConfiguration(
                issuer = config.getString("jwt.issuer"),
                secret = config.getString("jwt.secret"),
                accessExpiry = config.getLong("jwt.accessExpiry"),
            )
    }
}
