package com.example.safetynet.login

import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.json.webtoken.JsonWebSignature
import org.apache.http.conn.ssl.DefaultHostnameVerifier
import org.springframework.stereotype.Component
import java.security.cert.X509Certificate

@Component
class GoogleJwsParser : JwsParser {

    override fun verifyCertificateHostname(hostname: String, cert: X509Certificate) {
        DefaultHostnameVerifier().verify("attest.android.com", cert)
    }

    override fun parse(token: String): JsonWebSignature {
        return JsonWebSignature.parser(GsonFactory.getDefaultInstance()).setPayloadClass(AttestationStatement::class.java)
                .parse(token)
    }
}