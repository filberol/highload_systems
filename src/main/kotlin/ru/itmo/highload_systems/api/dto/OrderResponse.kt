package ru.itmo.highload_systems.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import ru.itmo.highload_systems.infra.model.enums.OrderStatus
import java.time.OffsetDateTime
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderResponse(
    val id: UUID,
    val status: OrderStatusRequestResponse,
    val size: Long,
    val departmentId: UUID,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
