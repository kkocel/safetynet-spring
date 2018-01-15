package com.example.safetynet.login

import com.google.api.client.json.webtoken.JsonWebSignature
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.security.GeneralSecurityException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLException

@Component
class JwtTokenVerifier(val jwsParser: JwsParser) {

    fun parseAndVerify(signedAttestationStatement: String): Mono<AttestationStatement> =
            Mono.fromCallable {
                val jws: JsonWebSignature

                try {
                    jws = jwsParser.parse(signedAttestationStatement)
                } catch (e: Exception) {
                    throw AttestationException("Invalid format of JWS.")
                }

                val cert: X509Certificate?
                try {
                    cert = jws.verifySignature()
                    if (cert == null) {
                        throw AttestationException("Signature verification failed.")
                    }
                } catch (e: GeneralSecurityException) {
                    throw AttestationException("Error during cryptographic verification of the JWS signature.")
                }

                try {
                    jwsParser.verifyCertificateHostname("attest.android.com", cert)
                } catch (e: SSLException) {
                    throw AttestationException("Certificate isn't issued for the hostname: attest.android.com.")
                }

                jws.payload as AttestationStatement
            }

}