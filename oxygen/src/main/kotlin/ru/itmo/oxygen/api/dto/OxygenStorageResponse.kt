package ru.itmo.oxygen.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OxygenStorageResponse(
    val id: UUID,
    val size: Long,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
