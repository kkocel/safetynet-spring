package com.example.safetynet.model

import com.google.api.client.json.webtoken.JsonWebToken
import com.google.api.client.util.Key

@Suppress("ArrayInDataClass", "unused")
data class AttestationStatement(
        @field:Key val nonce: String = "",
        @field:Key val timestampMs: Long = 0,
        @field:Key val apkPackageName: String = "",
        @field:Key val apkCertificateDigestSha256: Array<String> = emptyArray(),
        @field:Key val apkDigestSha256: String = "",
        @field:Key val ctsProfileMatch: Boolean = false,
        @field:Key val basicIntegrity: Boolean = false
) : JsonWebToken.Payload()
