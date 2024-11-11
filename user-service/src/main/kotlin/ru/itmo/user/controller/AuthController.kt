package ru.itmo.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.user.exceptions.UserAlreadyExistsException
import ru.itmo.user.model.dto.JwtRequestDto
import ru.itmo.user.model.dto.UserRegisterRequestDto
import ru.itmo.user.services.AuthService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @Operation(description = "Log in", summary = "Log in to the system", tags = ["Auth"])
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Success"), ApiResponse(
            responseCode = "401",
            description = "Incorrect username or password"
        )]
    )
    @PostMapping("/login")
    fun createAuthToken(@RequestBody request: @Valid JwtRequestDto?): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(authService.login(request!!.username, request!!.password))
        } catch (e: BadCredentialsException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверные имя пользователя или пароль")
        }
    }

    @Operation(description = "Register", summary = "New user register", tags = ["Auth"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "User has successfully registered"
        ), ApiResponse(responseCode = "400", description = "User with this username already exists")]
    )
    @PostMapping("/register")
    fun register(@RequestBody userRegisterRequest: @Valid UserRegisterRequestDto): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(authService.register(userRegisterRequest))
        } catch (e: UserAlreadyExistsException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}