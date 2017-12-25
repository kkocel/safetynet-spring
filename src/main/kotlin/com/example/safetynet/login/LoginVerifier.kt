package com.example.safetynet.login

import com.example.safetynet.nonce.NonceProvider
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
internal class LoginVerifier(
        val nonceProvider: NonceProvider,
        val jwtTokenVerifier: JwtTokenVerifier,
        val attestationStatementVerifier: AttestationStatementVerifier) {

    private val base64Decoder: Base64.Decoder = Base64.getDecoder()

    companion object {
        // TODO: change app package name and certificate digest
        const val PACKAGE_NAME: String = "com.example.safetynet"
        const val APK_CERT_DIGEST = "92:C4:DA:1D:B7:6C:68:24:58:82:6B:DF:12:C9:DC:D7:4E:A1:CA:1A:AD:0B:79:7D:85:EF:2A:3A:57:B9:D9:07"
    }

    fun loginUser(login: String, password: String, jwt: String): Mono<Boolean> =

            // TODO: check if a valid user provided a correct password

            jwtTokenVerifier.parseAndVerify(jwt)
                    .map { attestationStatement ->
                        val decodedNonce = String(base64Decoder.decode(attestationStatement.nonce))
                        val userIdentifier = nonceProvider.getUserIdentifierIfPresent(decodedNonce)
                                ?: throw AttestationException("Nonce is not present in cache.")

                        if (userIdentifier.login != login) {
                            throw AttestationException("Login from nonce cache mismatched with provided one.")
                        }

                        attestationStatementVerifier.verify(attestationStatement)

                        true
                    }
}