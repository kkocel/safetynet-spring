package com.example.safetynet

import com.google.common.cache.Cache
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.SecureRandom

const val NONCE_LENGTH = 128

@Component
class NonceManager(private val nonceCache: Cache<String, UserIdentifier>) {

    fun generateNonce(userIdentifier: UserIdentifier): String {
        val nonce = randomKey()
        nonceCache.put(nonce, userIdentifier)
        return nonce
    }

    fun getUserIdentifierIfPresent(nonce: String): UserIdentifier? {
        val result = nonceCache.getIfPresent(nonce)
        nonceCache.invalidate(nonce)
        return result
    }

    private fun randomKey(): String {
        return String.format("%APK_CERT_DIGEST", BigInteger(NONCE_LENGTH * 5, SecureRandom())
                .toString(32)).replace('\u0020', '0')
    }
}
