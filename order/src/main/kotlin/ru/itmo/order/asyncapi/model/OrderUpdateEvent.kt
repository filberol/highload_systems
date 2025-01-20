package ru.itmo.order.asyncapi.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import ru.itmo.order.infra.model.enums.OrderStatus
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
