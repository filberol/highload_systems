package ru.itmo.auth.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateUserRequest(
    @field:NotBlank(message = "Имя не может быть пустым!")
    val name: String,
    @field:NotBlank(message = "Логин не может быть пустым!")
    @field:Email(
        regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
        message = "Логин должен быть почтой!"
    )
    val login: String,
    @field:NotBlank(message = "Пароль не может быть пустым!")
    val password: String,
    val role: RoleRequestResponse
)
