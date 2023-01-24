package com.example.util

import java.security.DigestException
import java.security.MessageDigest

object HashUtil {
    fun hashSHA256(msg: String): String =
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(msg.toByteArray())
            md.digest().toHex()
        } catch (e: CloneNotSupportedException) {
            throw DigestException("couldn't make digest of partial content");
        }

    private fun ByteArray.toHex(): String {
        return this.joinToString("") { "%02x".format(it) }
    }
}