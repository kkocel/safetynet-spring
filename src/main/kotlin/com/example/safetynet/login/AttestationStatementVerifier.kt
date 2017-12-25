package com.example.safetynet.login

import com.google.common.io.BaseEncoding
import org.springframework.stereotype.Component
import java.util.*

@Component
internal class AttestationStatementVerifier(val timeProvider: TimeProvider) {

    private val base64Decoder: Base64.Decoder = Base64.getDecoder()
    private val base16Encoder: BaseEncoding = BaseEncoding.base16()


     fun verify(attestationStatement: AttestationStatement) {

        if (attestationStatement.apkPackageName != LoginVerifier.PACKAGE_NAME) {
            throw AttestationException("Package name is different.")
        }

        if (getCertificateDigestSha256(attestationStatement) != LoginVerifier.APK_CERT_DIGEST) {
            throw AttestationException("Apk certificate fingerprint mismatch.")
        }

        if (!(attestationStatement.basicIntegrity && attestationStatement.ctsProfileMatch)) {
            throw AttestationException("Basic integrity or CTS profile match is not passed.")
        }

        if (attestationStatement.timestampMs < timeProvider.getTimestampThreshold()) {
            throw AttestationException("Attestation statement is older than 1 hour.")
        }
    }

    private fun getCertificateDigestSha256(attestationStatement: AttestationStatement): String {
        val decode: ByteArray = base64Decoder.decode(attestationStatement.apkCertificateDigestSha256[0])
        return base16Encoder.withSeparator(":", 2).encode(decode)
    }
}