package ru.itmo.notification.asyncapi.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderUpdateEvent(
    val id: UUID? = null,
    val userId: UUID? = null,
    val departmentId: UUID? = null,
    val status: OrderStatus? = null,
    val updatedAt: OffsetDateTime? = null
)
