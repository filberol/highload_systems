package ru.itmo.highload_systems.api.dto

import java.time.OffsetDateTime
import java.util.UUID

data class RoomResponse(
    val id: UUID,
    val number: Long,
    val size: Long,
    val capacity: Long,
    val avgPersonNorm: Long,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
