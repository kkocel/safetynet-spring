package com.example.safetynet.repository

import com.google.api.client.json.webtoken.JsonWebToken
import com.google.api.client.util.Key

@Suppress("ArrayInDataClass", "unused")
data class AttestationStatement(
        @field:Key var nonce: String = "",
        @field:Key var timestampMs: Long = 0,
        @field:Key var apkPackageName: String = "",
        @field:Key var apkCertificateDigestSha256: Array<String> = emptyArray(),
        @field:Key var apkDigestSha256: String = "",
        @field:Key var ctsProfileMatch: Boolean = false,
        @field:Key var basicIntegrity: Boolean = false
) : JsonWebToken.Payload()
