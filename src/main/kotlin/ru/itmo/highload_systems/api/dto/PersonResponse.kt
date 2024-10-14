package ru.itmo.highload_systems.api.dto

import java.util.UUID

data class PersonResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val isAlive: Boolean
)
