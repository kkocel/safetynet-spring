package com.example.safetynet.cache

import com.example.safetynet.nonce.UserIdentifier
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

// TODO: In production environment this should be replaced with standalone cache shared among instances eg. Redis
@Component
internal class MemoryNonceCache(val cache: Cache<String, UserIdentifier> = CacheBuilder.newBuilder()
        .expireAfterWrite(15, TimeUnit.MINUTES)
        .build()) : NonceCache {

    override fun invalidate(nonce: String) = cache.invalidate(nonce)

    override fun put(nonce: String, userIdentifier: UserIdentifier) = cache.put(nonce, userIdentifier)

    override fun getIfPresent(nonce: String): UserIdentifier? = cache.getIfPresent(nonce)
}