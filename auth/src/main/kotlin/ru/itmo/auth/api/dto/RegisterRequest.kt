package ru.itmo.auth.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RegisterRequest(
    @NotBlank(message = "name cannot be blank!")
    val name: String,
    @NotBlank(message = "login cannot be blank!")
    @Email(
        regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
        message = "login must be an email!"
    )
    val login: String,
    @NotBlank(message = "password cannot be blank!")
    val password: String
)
