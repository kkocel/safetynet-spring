package com.example.safetynet.cache

import com.example.safetynet.nonce.UserIdentifier

interface NonceCache {

    fun invalidate(nonce: String)

    fun put(nonce: String, userIdentifier: UserIdentifier)

    fun getIfPresent(nonce: String): UserIdentifier?
}