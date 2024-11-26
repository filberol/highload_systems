package ru.itmo.order.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderResponse(
    val id: UUID,
    val status: OrderStatusRequestResponse,
    val departmentId: UUID,
    val userId: UUID,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
