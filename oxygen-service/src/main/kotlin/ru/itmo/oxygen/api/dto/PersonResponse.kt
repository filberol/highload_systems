package ru.itmo.highload_systems.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PersonResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val isAlive: Boolean
)
