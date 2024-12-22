package ru.itmo.files.asyncapi.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderUpdateEvent(
    val id: UUID,
    val userId: UUID,
    val departmentId: UUID,
    val status: OrderStatus,
    val updatedAt: OffsetDateTime
)
