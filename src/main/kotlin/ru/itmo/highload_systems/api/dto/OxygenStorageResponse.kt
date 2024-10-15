package ru.itmo.highload_systems.api.dto

import java.time.OffsetDateTime
import java.util.*

data class OxygenStorageResponse(
    val id: UUID,
    val size: Long,
    val capacity: Long,
    val departmentId: UUID,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
