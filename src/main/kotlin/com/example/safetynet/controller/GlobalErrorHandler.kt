package com.example.safetynet.controller

import com.example.safetynet.repository.AttestationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class GlobalErrorHandlerAdvice {

    @ExceptionHandler(AttestationException::class)
    @ResponseBody
    fun attestationException(ex: AttestationException): ResponseEntity<String> =
        ResponseEntity(ex.message ?: "", HttpStatus.UNAUTHORIZED)
}