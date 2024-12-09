package ru.itmo.department.clients.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserResponse(
    val id: UUID,
    val name: String,
    val login: String,
    val password: String,
    val role: RoleRequestResponse
)
