package ru.itmo.auth.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
enum class RoleRequestResponse {
    ADMIN, USER, SUPPLIER, MANAGER
}