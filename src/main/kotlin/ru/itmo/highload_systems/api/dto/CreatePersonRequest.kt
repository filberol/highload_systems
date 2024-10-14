package ru.itmo.highload_systems.api.dto

import jakarta.validation.constraints.NotBlank

data class CreatePersonRequest(
    @NotBlank
    val firstName: String,
    @NotBlank
    val lastName: String,
    val middleName: String?
)