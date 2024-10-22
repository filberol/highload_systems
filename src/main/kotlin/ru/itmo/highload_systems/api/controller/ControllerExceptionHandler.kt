package ru.itmo.highload_systems.api.controller

import org.springdoc.api.ErrorMessage
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.security.InvalidParameterException


@RestControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleException(exception: NotFoundException): ResponseEntity<ErrorMessage> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorMessage(exception.message))
    }

    @ExceptionHandler(InvalidParameterException::class)
    fun handleException(exception: InvalidParameterException): ResponseEntity<ErrorMessage> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage(exception.message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleException(exception: IllegalArgumentException): ResponseEntity<ErrorMessage> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage(exception.message))
    }
}