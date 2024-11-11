package ru.itmo.user.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class UserRegisterRequestDto(
    val username: @NotBlank String,
    val email: @NotBlank @Pattern(regexp = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)") String,
    val password: @NotBlank String
)
