package ru.itmo.highload_systems.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OxygenSupplyResponse(
    val id: UUID,
    val size: Long,
    val oxygenStorageId: UUID? = null,
    val departmentId: UUID,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
