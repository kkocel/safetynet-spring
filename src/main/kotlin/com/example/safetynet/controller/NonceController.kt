package com.example.safetynet.controller

import com.example.safetynet.login.LoginVerifier
import com.example.safetynet.nonce.NonceProvider
import com.example.safetynet.nonce.UserIdentifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
internal class NonceController(val nonceProvider: NonceProvider, val loginVerifier: LoginVerifier) {

    @GetMapping("/nonce")
    fun getNonce(@RequestParam login: String, @RequestParam deviceId: String): Mono<String> =
            nonceProvider.generateNonce(UserIdentifier(login, deviceId))

    @PostMapping("/login")
    fun login(request: LoginRequest): Mono<Void> =
            loginVerifier.loginUser(request.login, request.password, request.jwt).then()
}

internal data class LoginRequest(val login: String, val password: String, val jwt: String)