package ru.itmo.highload_systems.api.dto

import jakarta.annotation.Nonnull
import jakarta.validation.constraints.Min
import java.util.*

data class CreateOrderRequest(
    @Min(1)
    val size: Long,
    @Nonnull
    val departmentId: UUID
)
