package com.example.safetynet.service

import com.example.safetynet.repository.UserIdentifier
import com.google.common.cache.Cache
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.math.BigInteger
import java.security.SecureRandom

@Component
class NonceService(val nonceRepository: Cache<String, UserIdentifier>) {

    companion object {
        val NONCE_LENGTH = 128
    }

    fun generateNonce(userIdentifier: UserIdentifier): Mono<String> =
            Mono.fromCallable {
                val nonce = randomKey()
                nonceRepository.put(nonce, userIdentifier)
                nonce
            }

    fun getUserIdentifierIfPresent(nonce: String): UserIdentifier? {
        val result = nonceRepository.getIfPresent(nonce)
        nonceRepository.invalidate(nonce)
        return result
    }

    private fun randomKey(): String {
        val key = BigInteger(NONCE_LENGTH * 5, SecureRandom()).toString(32).replace('\u0020', '0')
        return "${key}APK_CERT_DIGEST"
    }
}
