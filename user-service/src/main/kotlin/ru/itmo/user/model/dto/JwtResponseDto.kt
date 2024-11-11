package ru.itmo.user.model.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonDeserialize
@JsonSerialize
data class JwtResponseDto(
    val token: String
)
