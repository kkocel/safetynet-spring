package com.example.safetynet.repository

import com.example.safetynet.model.AttestationException
import com.example.safetynet.model.AttestationStatement
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.json.webtoken.JsonWebSignature
import org.apache.http.conn.ssl.DefaultHostnameVerifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLException

@Component
class JwtTokenVerifierRepository {

    fun parseAndVerify(signedAttestationStatement: String): Mono<AttestationStatement> =
            Mono.fromCallable {
                val jws: JsonWebSignature

                try {
                    jws = JsonWebSignature.parser(GsonFactory.getDefaultInstance()).setPayloadClass(AttestationStatement::class.java)
                            .parse(signedAttestationStatement)
                } catch (e: IOException) {
                    throw AttestationException("Invalid format of JWS.")
                }

                val cert: X509Certificate
                try {
                    cert = jws.verifySignature()
                    if (cert == null) {
                        throw IllegalArgumentException("Signature verification failed.")
                    }
                } catch (e: GeneralSecurityException) {
                    throw IllegalArgumentException("Error during cryptographic verification of the JWS signature.")
                }

                // Verify the hostname of the certificate.
                if (!verifyHostname(cert)) {
                    throw IllegalArgumentException("Certificate isn't issued for the hostname: attest.android.com.")
                }

                jws.payload as AttestationStatement
            }

    private fun verifyHostname(leafCert: X509Certificate): Boolean =
            try {
                DefaultHostnameVerifier().verify("attest.android.com", leafCert)
                true
            } catch (e: SSLException) {
                false
            }
}