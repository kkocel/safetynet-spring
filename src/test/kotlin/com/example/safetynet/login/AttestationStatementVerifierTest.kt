package com.example.safetynet.login


import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.matchers.startWith
import org.junit.Test
import java.util.*

class AttestationStatementVerifierTest {
    private val attestationStatementVerifier = AttestationStatementVerifier(TimeProvider())

    @Test
    fun `should fail with wrong package`() {
        // given
        val attestationStatement = AttestationStatement(
                apkPackageName = "com.foo.bar")

        val exception = shouldThrow<AttestationException> {
            // when
            attestationStatementVerifier.verify(attestationStatement)
        }

        // then
        exception.message!! should startWith("Package name is different.")
    }

    @Test
    fun `should fail with wrong apk certificate`() {
        // given
        val attestationStatement = AttestationStatement(
                apkPackageName = LoginVerifier.PACKAGE_NAME,
                apkCertificateDigestSha256 = arrayOf(""))

        val exception = shouldThrow<AttestationException> {
            // when
            attestationStatementVerifier.verify(attestationStatement)
        }

        // then
        exception.message!! should startWith("Apk certificate fingerprint mismatch.")
    }

    @Test
    fun `should fail with CTS profile mismatch`() {
        // given
        val attestationStatement = AttestationStatement(
                apkPackageName = LoginVerifier.PACKAGE_NAME,
                apkCertificateDigestSha256 = arrayOf("ksTaHbdsaCRYgmvfEsnc106hyhqtC3l9he8qOle52Qc="),
                ctsProfileMatch = false,
                basicIntegrity = false)

        val exception = shouldThrow<AttestationException> {
            // when
            attestationStatementVerifier.verify(attestationStatement)
        }

        // then
        exception.message!! should startWith("Basic integrity or CTS profile match is not passed.")
    }

    @Test
    fun `should fail with basic integrity mismatch`() {
        // given
        val attestationStatement = AttestationStatement(
                apkPackageName = LoginVerifier.PACKAGE_NAME,
                apkCertificateDigestSha256 = arrayOf("ksTaHbdsaCRYgmvfEsnc106hyhqtC3l9he8qOle52Qc="),
                basicIntegrity = false,
                ctsProfileMatch = true)

        val exception = shouldThrow<AttestationException> {
            // when
            attestationStatementVerifier.verify(attestationStatement)
        }

        // then
        exception.message!! should startWith("Basic integrity or CTS profile match is not passed.")
    }

    @Test
    fun `should fail with CTS profile mismatch even if basic integrity is ok`() {
        // given
        val attestationStatement = AttestationStatement(
                apkPackageName = LoginVerifier.PACKAGE_NAME,
                apkCertificateDigestSha256 = arrayOf("ksTaHbdsaCRYgmvfEsnc106hyhqtC3l9he8qOle52Qc="),
                basicIntegrity = true,
                ctsProfileMatch = false)

        val exception = shouldThrow<AttestationException> {
            // when
            attestationStatementVerifier.verify(attestationStatement)
        }

        // then
        exception.message!! should startWith("Basic integrity or CTS profile match is not passed.")
    }

    @Test
    fun `should fail with timestamp too old`() {
        // given
        val attestationStatement = AttestationStatement(
                apkPackageName = LoginVerifier.PACKAGE_NAME,
                apkCertificateDigestSha256 = arrayOf("ksTaHbdsaCRYgmvfEsnc106hyhqtC3l9he8qOle52Qc="),
                ctsProfileMatch = true,
                basicIntegrity = true)

        val exception = shouldThrow<AttestationException> {
            // when
            attestationStatementVerifier.verify(attestationStatement)
        }

        // then
        exception.message!! should startWith("Attestation statement is older than 1 hour.")
    }

    @Test
    fun `should pass verification`() {
        // given
        val attestationStatement = AttestationStatement(
                apkPackageName = LoginVerifier.PACKAGE_NAME,
                apkCertificateDigestSha256 = arrayOf("ksTaHbdsaCRYgmvfEsnc106hyhqtC3l9he8qOle52Qc="),
                ctsProfileMatch = true,
                basicIntegrity = true,
                timestampMs = Date().time)

        // when
        attestationStatementVerifier.verify(attestationStatement)


        // then no exception
    }
}