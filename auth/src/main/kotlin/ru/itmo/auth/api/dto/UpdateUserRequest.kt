package ru.itmo.auth.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UpdateUserRequest(
    @field:NotBlank(message = "Имя не может быть пустым!")
    @field:Email(
        regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
        message = "Логин должен быть почтой!"
    )
    val login: String,
    val role: RoleRequestResponse
)
