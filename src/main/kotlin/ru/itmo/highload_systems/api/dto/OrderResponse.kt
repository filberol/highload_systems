package ru.itmo.highload_systems.api.dto

import ru.itmo.highload_systems.infra.model.enums.OrderStatus
import java.time.OffsetDateTime
import java.util.*

data class OrderResponse(
    val id: UUID,
    val status: OrderStatusRequestResponse,
    val size: Long,
    val departmentId: UUID,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
