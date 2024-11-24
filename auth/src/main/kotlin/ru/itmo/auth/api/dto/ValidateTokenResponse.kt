package ru.itmo.auth.api.dto

import java.util.UUID

data class ValidateTokenResponse(
    val userId: UUID? = null,
    val login: String? = null,
    val authorities: List<RoleRequestResponse>? = null,
    val isAuthenticated: Boolean
)