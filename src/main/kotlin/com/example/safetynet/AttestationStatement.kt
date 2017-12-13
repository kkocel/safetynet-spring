package com.example.safetynet

import com.google.api.client.json.webtoken.JsonWebToken
import com.google.api.client.util.Key

@Suppress("ArrayInDataClass", "unused")
data class AttestationStatement(
        @field:Key var nonce: String,
        @field:Key var timestampMs: Long,
        @field:Key var apkPackageName: String,
        @field:Key var apkCertificateDigestSha256: Array<String>,
        @field:Key var apkDigestSha256: String,
        @field:Key var ctsProfileMatch: Boolean,
        @field:Key var basicIntegrity: Boolean
) : JsonWebToken.Payload() {
    constructor() : this("", 0, "", emptyArray(), "", false, false)
}
