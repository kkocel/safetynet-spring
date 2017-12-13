package com.example.safetynet

import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.json.webtoken.JsonWebSignature
import org.apache.http.conn.ssl.DefaultHostnameVerifier
import org.springframework.stereotype.Component
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLException

@Component
class JwtTokenVerifier {

    fun parseAndVerify(signedAttestationStatment: String): AttestationStatement {

        val jws: JsonWebSignature

        try {
            jws = JsonWebSignature.parser(GsonFactory.getDefaultInstance()).setPayloadClass(AttestationStatement::class
                    .java)
                    .parse(signedAttestationStatment)
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

        return jws.payload as AttestationStatement
    }


    private fun verifyHostname(leafCert: X509Certificate): Boolean {
        try {
            DefaultHostnameVerifier().verify("attest.android.com", leafCert)
            return true
        } catch (e: SSLException) {
        }

        return false
    }
}