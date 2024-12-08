package ru.itmo.auth.api.controller.handler

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.HandlerExceptionResolver
import ru.itmo.auth.api.controller.SecurityController
import ru.itmo.auth.api.controller.UserController


@RestControllerAdvice(assignableTypes = [UserController::class, SecurityController::class])
class ControllerExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleException(exception: NoSuchElementException): ResponseEntity<String> {
        return ResponseEntity(exception.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(exception.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerException(exception: MethodArgumentNotValidException): ResponseEntity<String> {
        return ResponseEntity(exception.bindingResult.fieldError!!.defaultMessage, HttpStatus.BAD_REQUEST)
    }
}