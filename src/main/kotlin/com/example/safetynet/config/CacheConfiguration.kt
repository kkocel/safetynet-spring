package com.example.safetynet.config

import com.example.safetynet.nonce.UserIdentifier
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
internal class CacheConfiguration {

    @Bean
    internal fun nonceCache(): Cache<String, UserIdentifier> {
        // TODO: In production environment this should be moved to standalone cache
        return CacheBuilder.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build()
    }
}