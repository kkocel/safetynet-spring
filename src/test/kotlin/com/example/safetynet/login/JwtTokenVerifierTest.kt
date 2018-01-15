package com.example.safetynet.login

import com.google.api.client.json.webtoken.JsonWebSignature
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.security.GeneralSecurityException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLException

class JwtTokenVerifierTest {
    private val jwsParser = mockk<JwsParser>()
    private val jwtTokenVerifier: JwtTokenVerifier = JwtTokenVerifier(jwsParser)

    @Test
    fun `should throw AttestationException when there is no certificate`() {
        // given
        val jsonWebSignature = mockk<JsonWebSignature>()
        every { jsonWebSignature.verifySignature() } returns null
        every { jwsParser.parse(any()) } returns jsonWebSignature

        // when
        val exception = shouldThrow<AttestationException> {
            jwtTokenVerifier.parseAndVerify("foo").block()
        }

        // then
        exception.message!! shouldBe "Signature verification failed."
    }

    @Test
    fun `should throw AttestationException when there is general security error`() {
        // given
        val jsonWebSignature = mockk<JsonWebSignature>()
        every { jsonWebSignature.verifySignature() } throws GeneralSecurityException("Some exception")
        every { jwsParser.parse(any()) } returns jsonWebSignature

        // when
        val exception = shouldThrow<AttestationException> {
            jwtTokenVerifier.parseAndVerify("foo").block()
        }

        // then
        exception.message!! shouldBe "Error during cryptographic verification of the JWS signature."
    }

    @Test
    fun `should throw AttestationException when certificate is issued for a different hostname`() {
        // given
        val jsonWebSignature = mockk<JsonWebSignature>()
        val x509Certificate = mockk<X509Certificate>()
        every { jsonWebSignature.verifySignature() } returns x509Certificate
        every { jwsParser.parse(any()) } returns jsonWebSignature
        every { jwsParser.verifyCertificateHostname(any(), any()) } throws SSLException("Some exception")

        // when
        val exception = shouldThrow<AttestationException> {
            jwtTokenVerifier.parseAndVerify("foo").block()
        }

        // then
        exception.message!! shouldBe "Certificate isn't issued for the hostname: attest.android.com."
    }
}

