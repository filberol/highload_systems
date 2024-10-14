package ru.itmo.highload_systems.api.dto

import java.util.*

data class CreatePersonResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val middleName: String?
)
