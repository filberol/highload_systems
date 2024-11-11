package ru.itmo.user.model.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.io.Serializable

@JsonDeserialize
@JsonSerialize
data class JwtRequestDto(
    val username: String,
    val password: String
): Serializable