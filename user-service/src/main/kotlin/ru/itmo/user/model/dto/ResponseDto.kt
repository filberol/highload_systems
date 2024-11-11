package ru.itmo.user.model.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.http.HttpStatus
import java.io.Serializable

@JsonSerialize
@JsonDeserialize
data class ResponseDto<T>(val body: T, val e: Throwable?, val code: HttpStatus) : Serializable
