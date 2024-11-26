package ru.itmo.auth.api.controller

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.itmo.auth.api.dto.CreateUserRequest
import ru.itmo.auth.api.dto.RegisterRequest
import ru.itmo.auth.api.dto.UpdateUserRequest
import ru.itmo.auth.api.dto.UserResponse
import ru.itmo.auth.domain.service.UserService
import java.util.UUID

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun create(@RequestBody request: CreateUserRequest): UserResponse {
        return userService.create(request)
    }

    @PutMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun update(@RequestBody request: UpdateUserRequest): UserResponse {
        return userService.update(request)
    }

    @GetMapping("/users/{login}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    fun findByLogin(@PathVariable login: String): UserResponse {
        return userService.findByLogin(login)
    }

    @Hidden
    @GetMapping("/users/id/{id}")
    fun findById(@PathVariable id: UUID): UserResponse {
        return userService.findById(id)
    }
}