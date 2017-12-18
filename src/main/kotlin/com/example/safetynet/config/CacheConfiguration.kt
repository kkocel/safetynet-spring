package com.example.safetynet.config

import com.example.safetynet.repository.UserIdentifier
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CacheConfiguration {

    @Bean
    fun cache(): Cache<String, UserIdentifier> {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build()
    }
}