package ru.itmo.user.model.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.io.Serializable

@JsonSerialize
@JsonDeserialize
data class UserUpdateBalanceDto (
    var userName: @NotBlank String,
    var newBalance: @NotNull @Min(0) Int,
): Serializable