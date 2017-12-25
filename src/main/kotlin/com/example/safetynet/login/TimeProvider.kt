package com.example.safetynet.login

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.ZonedDateTime

@Component
@Profile("production")
internal class TimeProvider {

    fun getTimestampThreshold(): Long {
        return ZonedDateTime.now().minusHours(1).toInstant().toEpochMilli()
    }
}