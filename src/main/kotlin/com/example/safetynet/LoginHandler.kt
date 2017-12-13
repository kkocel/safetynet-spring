package com.example.safetynet

import com.google.common.io.BaseEncoding
import org.springframework.stereotype.Component
import java.util.*

private const val PACKAGE_NAME: String = "com.example.safetynet"

private const val APK_CERT_DIGEST = "FO:BA:"

@Component
class LoginHandler(val nonceManager: NonceManager, val jwtTokenVerifier: JwtTokenVerifier) {


    fun loginUser(
            login: String,
            password: String,
            jwt: String): Boolean {

        // TODO: check if a valid user provided a correct password

        try {
            val attestationStatement = jwtTokenVerifier.parseAndVerify(jwt)


            val userIdentifier = nonceManager.getUserIdentifierIfPresent(attestationStatement.nonce) ?: throw AttestationException("Nonce is not present in cache.")

            if (userIdentifier.login != login) {
                throw AttestationException("Login from nonce cache mismatched with provided one.")
            }

            if (attestationStatement.apkPackageName != PACKAGE_NAME) {
                throw AttestationException("Package name is different.")
            }

            if(getCertificateDigestSha256(attestationStatement) != APK_CERT_DIGEST){
                throw AttestationException("Apk certificate fingerprint mismatch.")
            }

            if (!(attestationStatement.basicIntegrity && attestationStatement.ctsProfileMatch)) {
                throw AttestationException("Basic integrity or CTS profile match is not passed.")
            }

            return true

        } catch (e: AttestationException) {
            // log it here
        }

        return false
    }

    private fun getCertificateDigestSha256(attestationStatement: AttestationStatement): String {
        val decode: ByteArray = Base64.getDecoder().decode(attestationStatement.apkCertificateDigestSha256[0])
        return BaseEncoding.base16().withSeparator(":", 2).encode(decode)
    }
}