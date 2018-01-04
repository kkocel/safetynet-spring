package com.example.safetynet

import com.example.safetynet.login.TimeProvider
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("test")
@Primary
internal class TimeTestProvider : TimeProvider() {
    var fakeCurrentTimestampMinusOneHOur: Long = 0


    override fun getTimestampThreshold(): Long {
        return fakeCurrentTimestampMinusOneHOur
    }
}