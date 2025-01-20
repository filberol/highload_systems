package ru.itmo.department.clients.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserResponse(
    var id: UUID,
    var name: String,
    var login: String,
    var password: String,
    var role: RoleRequestResponse
)
