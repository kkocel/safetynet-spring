package com.example.safetynet.login

import com.google.api.client.json.webtoken.JsonWebSignature
import java.security.cert.X509Certificate

interface JwsParser {
    fun parse(token: String): JsonWebSignature

    fun verifyCertificateHostname(hostname: String, cert: X509Certificate)
}