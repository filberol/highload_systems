package ru.itmo.auth.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.auth.api.dto.AuthRequest
import ru.itmo.auth.api.dto.AuthResponse
import ru.itmo.auth.api.dto.RegisterRequest
import ru.itmo.auth.domain.service.AuthService


@RestController
@RequestMapping("/auth")
class SecurityController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@Validated @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/authenticate")
    fun authenticate(@Validated @RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.authenticate(request))
    }
}