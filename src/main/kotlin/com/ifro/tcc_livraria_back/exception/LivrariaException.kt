package com.ifro.tcc_livraria_back.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

class LivrariaException(
    val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    override val message: String
) : RuntimeException(message)

@ControllerAdvice
class LivrariaExceptionHandler {

    @ExceptionHandler(LivrariaException::class)
    fun handleException(exception: LivrariaException) : ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(exception.status.value(), exception.message)
        return ResponseEntity(errorResponse, exception.status)
    }
    data class ErrorResponse(val status: Int, val message: String)
}