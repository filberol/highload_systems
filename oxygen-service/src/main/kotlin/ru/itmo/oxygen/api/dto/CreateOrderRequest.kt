package ru.itmo.highload_systems.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.annotation.Nonnull
import jakarta.validation.constraints.Min
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateOrderRequest(
    @Min(1)
    val size: Long,
    @Nonnull
    val departmentId: UUID
)
