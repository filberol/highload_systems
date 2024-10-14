package ru.itmo.highload_systems.api.dto

import java.time.OffsetDateTime
import java.util.UUID

data class OxygenSupplyResponse(
    val id: UUID,
    val size: Long,
    val oxygenStorageId: UUID? = null,
    val departmentId: UUID,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
