package ru.itmo.highload_systems.api.dto

import java.time.OffsetDateTime
import java.util.UUID

data class DepartmentResponse(
    val id: UUID,
    val name: String,
    val createdAt: OffsetDateTime
)
