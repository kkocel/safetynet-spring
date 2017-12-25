package com.example.safetynet

import com.example.safetynet.nonce.NonceProvider
import com.example.safetynet.nonce.UserIdentifier
import com.google.common.cache.Cache
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("test")
internal class NonceTestProvider(nonceCache: Cache<String, UserIdentifier>) : NonceProvider(nonceCache) {

    fun putNonceWithIdentifier(nonce: String, userIdentifier: UserIdentifier) {
        nonceCache.put(nonce, userIdentifier)
    }
}

