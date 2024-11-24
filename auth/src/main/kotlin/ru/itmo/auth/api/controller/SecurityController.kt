package ru.itmo.auth.api.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.itmo.auth.api.dto.*
import ru.itmo.auth.domain.service.AuthService
import ru.itmo.auth.domain.service.UserService
import java.util.*


@RestController
@RequestMapping("/auth")
class SecurityController(
    private val authService: AuthService,
    private val userService: UserService
) {

    @PostMapping("/register")
    fun register(@RequestBody @Validated request: RegisterRequest): ResponseEntity<AuthResponse> {
        return authService.register(request)
            .map { response -> ResponseEntity.ok(response) }
            .orElse(ResponseEntity.badRequest().build())
    }

    @PostMapping("/authenticate")
    fun authenticate(
        @RequestBody @Validated request: AuthRequest
    ): ResponseEntity<AuthResponse> {
        return authService.authenticate(request)
            .map { response -> ResponseEntity.ok(response) }
            .orElse(ResponseEntity.badRequest().build())
    }

    @GetMapping("/validate")
    fun validateToken(httpServletRequest: HttpServletRequest): ResponseEntity<ValidateTokenResponse> {
        val username = httpServletRequest.getAttribute("username") as String
        if (username.isEmpty()) {
            return ResponseEntity.ok(ValidateTokenResponse(isAuthenticated = false))
        } else {
            val authorities = httpServletRequest.getAttribute("authorities")
            val userId = httpServletRequest.getAttribute("userId") as UUID
            return ResponseEntity.ok(
                ValidateTokenResponse(
                    userId = userId,
                    login = username,
                    authorities = authorities as List<RoleRequestResponse>?,
                    isAuthenticated = true
                )
            )
        }
    }

}