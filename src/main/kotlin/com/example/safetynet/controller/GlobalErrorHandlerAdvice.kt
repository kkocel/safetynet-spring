package com.example.safetynet.controller

import com.example.safetynet.login.AttestationException
import com.google.gson.Gson
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
internal class GlobalErrorHandlerAdvice {

    @ExceptionHandler(AttestationException::class)
    @ResponseBody
    fun attestationException(ex: AttestationException): ResponseEntity<String> =
            ResponseEntity(Gson().toJson(ErrorResponse(ex.message)), HttpStatus.UNAUTHORIZED)
}

internal data class ErrorResponse(val message: String?)