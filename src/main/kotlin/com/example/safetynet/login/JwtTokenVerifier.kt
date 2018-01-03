package com.example.safetynet.login

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
class JwtTokenVerifier {

    fun parseAndVerify(signedAttestationStatement: String): Mono<AttestationStatement> =
            Mono.fromCallable {
                val jws: JsonWebSignature

                try {
                    jws = JsonWebSignature.parser(GsonFactory.getDefaultInstance()).setPayloadClass(AttestationStatement::class.java)
                            .parse(signedAttestationStatement)
                } catch (e: IOException) {
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
                    DefaultHostnameVerifier().verify("attest.android.com", cert)
                } catch (e: SSLException) {
                    throw AttestationException("Certificate isn't issued for the hostname: attest.android.com.")
                }

                jws.payload as AttestationStatement
            }

}