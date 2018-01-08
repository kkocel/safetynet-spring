package com.example.safetynet

import com.example.safetynet.login.TimeProvider
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
internal class TimeTestProvider : TimeProvider() {
    var fakeCurrentTimestampMinusOneHOur: Long = 0


    override fun getTimestampThreshold(): Long {
        return fakeCurrentTimestampMinusOneHOur
    }
}