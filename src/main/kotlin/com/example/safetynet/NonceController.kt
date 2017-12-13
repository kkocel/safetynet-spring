package com.example.safetynet

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class NonceController(val nonceManager: NonceManager, val loginHandler: LoginHandler) {

    @GetMapping("/nonce")
    fun getNonce(@RequestParam login: String, @RequestParam deviceId: String): Mono<String> = Mono.just(nonceManager.generateNonce(UserIdentifier(login, deviceId)))

    @PostMapping("/login")
    fun login(@RequestParam login: String, @RequestParam password: String, @RequestParam jwt: String): Mono<ResponseEntity<Void>> = Mono.just(responseEntity(login, password, jwt))

    private fun responseEntity(login: String, password: String, jwt: String): ResponseEntity<Void> =
            if (loginHandler.loginUser(login, password, jwt)) ResponseEntity.ok().build() else ResponseEntity.status(HttpStatus.FORBIDDEN).build()
}