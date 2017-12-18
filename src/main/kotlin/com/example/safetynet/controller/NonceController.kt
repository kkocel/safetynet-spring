package com.example.safetynet.controller

import com.example.safetynet.repository.UserIdentifier
import com.example.safetynet.service.LoginService
import com.example.safetynet.service.NonceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class NonceController(val nonceService: NonceService, val loginService: LoginService) {

    @GetMapping("/nonce")
    fun getNonce(@RequestParam login: String, @RequestParam deviceId: String): Mono<String> =
            nonceService.generateNonce(UserIdentifier(login, deviceId))

    @PostMapping("/login")
    fun login(request: LoginRequest): Mono<Void> =
            loginService.loginUser(request.login, request.password, request.jwt)
                    .map { null }
}

data class LoginRequest(val login: String, val password:String, val jwt:String)