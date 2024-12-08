package ru.itmo.auth.api.controller

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.itmo.auth.api.dto.CreateUserRequest
import ru.itmo.auth.api.dto.UpdateUserRequest
import ru.itmo.auth.api.dto.UserResponse
import ru.itmo.auth.domain.service.UserService
import java.util.*

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun create(@Validated @RequestBody request: CreateUserRequest): UserResponse {
        return userService.create(request)
    }

    @PutMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun update(@Validated @RequestBody request: UpdateUserRequest): UserResponse {
        return userService.update(request)
    }

    @GetMapping("/users/{login}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun findByLogin(
        @NotBlank(message = "Имя не может быть пустым!")
        @Email(
            regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Логин должен быть почтой!"
        )
        @PathVariable login: String
    ): UserResponse {
        return userService.findByLogin(login)
    }

    @GetMapping("users/id/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(userService.findById(id))
    }
}