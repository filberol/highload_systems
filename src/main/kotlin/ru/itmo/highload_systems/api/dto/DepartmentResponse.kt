package ru.itmo.highload_systems.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DepartmentResponse(
    val id: UUID,
    val name: String,
    val createdAt: OffsetDateTime
)
