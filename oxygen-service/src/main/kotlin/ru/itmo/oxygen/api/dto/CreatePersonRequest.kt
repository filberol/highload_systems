package ru.itmo.highload_systems.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotBlank

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreatePersonRequest(
    @NotBlank
    val firstName: String,
    @NotBlank
    val lastName: String,
    val middleName: String?
)