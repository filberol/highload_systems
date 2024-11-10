package ru.itmo.highload_systems.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
enum class OrderStatusRequestResponse {
    NEW,
    OXYGEN_WAITING,
    CANCEL,
    DEATH,
    DONE
}