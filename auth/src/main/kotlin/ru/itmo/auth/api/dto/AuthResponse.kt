package ru.itmo.auth.api.dto

import java.util.UUID

data class AuthResponse(
    val userId: UUID,
    val token: String
)