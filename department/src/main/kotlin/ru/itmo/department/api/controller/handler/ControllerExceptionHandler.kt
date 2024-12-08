package ru.itmo.department.api.controller.handler

import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.itmo.department.api.controller.DepartmentController
import ru.itmo.department.api.controller.RoomController
import ru.itmo.department.clients.exception.InternalServerException


@RestControllerAdvice(assignableTypes = [DepartmentController::class, RoomController::class])
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

    @ExceptionHandler(FeignException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(exception: FeignException): ResponseEntity<String> {
        return ResponseEntity("Пользователь не найден", HttpStatus.valueOf(exception.status()))
    }
}