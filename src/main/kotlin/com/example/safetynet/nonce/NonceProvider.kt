package com.example.safetynet.nonce

import com.google.common.cache.Cache
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.math.BigInteger
import java.security.SecureRandom

@Component
@Profile("production")
internal class NonceProvider(val nonceCache: Cache<String, UserIdentifier>) {

    fun generateNonce(userIdentifier: UserIdentifier): Mono<String> =
            Mono.fromCallable {
                val nonce = randomKey()
                nonceCache.put(nonce, userIdentifier)
                nonce
            }

    internal fun getUserIdentifierIfPresent(nonce: String): UserIdentifier? {
        val userIdentifier = nonceCache.getIfPresent(nonce)
        nonceCache.invalidate(nonce)
        return userIdentifier
    }

    private fun randomKey(): String {
        val length = 128
        return BigInteger(length * 5, SecureRandom()).toString(32).replace('\u0020', '0')
    }
}
