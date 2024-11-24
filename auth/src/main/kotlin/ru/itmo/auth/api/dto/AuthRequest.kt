package ru.itmo.auth.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AuthRequest(
    @NotBlank(message = "login cannot be blank!")
    @Email(
        regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
        message = "login must be an email!"
    )
    val login: String,
    @NotBlank(message = "password cannot be blank!")
    val password: String
)