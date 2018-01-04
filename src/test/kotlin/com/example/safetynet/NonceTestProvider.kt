package com.example.safetynet

import com.example.safetynet.cache.NonceCache
import com.example.safetynet.nonce.NonceProvider
import com.example.safetynet.nonce.UserIdentifier
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("test")
internal class NonceTestProvider(nonceCache: NonceCache) : NonceProvider(nonceCache) {

    fun putNonceWithIdentifier(nonce: String, userIdentifier: UserIdentifier) {
        nonceCache.put(nonce, userIdentifier)
    }
}

