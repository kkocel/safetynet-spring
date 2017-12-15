package com.example.safetynet.service

import com.example.safetynet.model.AttestationException
import com.example.safetynet.model.AttestationStatement
import com.example.safetynet.repository.JwtTokenVerifierRepository
import com.google.common.io.BaseEncoding
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*


@Component
class LoginService(val nonceService: NonceService, val jwtTokenVerifierRepository: JwtTokenVerifierRepository) {

    companion object {
        val PACKAGE_NAME: String = "com.example.safetynet"
        val APK_CERT_DIGEST = "FO:BA:"
    }

    fun loginUser(login: String, password: String, jwt: String): Mono<Boolean> =

            // TODO: check if a valid user provided a correct password
            jwtTokenVerifierRepository.parseAndVerify(jwt)
                    .map { attestationStatement ->
                        val userIdentifier = nonceService.getUserIdentifierIfPresent(attestationStatement.nonce)
                                ?: throw AttestationException("Nonce is not present in cache.")

                        if (userIdentifier.login != login) {
                            throw AttestationException("Login from nonce cache mismatched with provided one.")
                        }

                        if (attestationStatement.apkPackageName != PACKAGE_NAME) {
                            throw AttestationException("Package name is different.")
                        }

                        if (getCertificateDigestSha256(attestationStatement) != APK_CERT_DIGEST) {
                            throw AttestationException("Apk certificate fingerprint mismatch.")
                        }

                        if (!(attestationStatement.basicIntegrity && attestationStatement.ctsProfileMatch)) {
                            throw AttestationException("Basic integrity or CTS profile match is not passed.")
                        }

                        true
                    }

    private fun getCertificateDigestSha256(attestationStatement: AttestationStatement): String {
        val decode: ByteArray = Base64.getDecoder().decode(attestationStatement.apkCertificateDigestSha256[0])
        return BaseEncoding.base16().withSeparator(":", 2).encode(decode)
    }
}