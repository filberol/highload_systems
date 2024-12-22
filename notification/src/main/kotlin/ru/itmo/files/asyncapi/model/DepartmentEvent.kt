package ru.itmo.files.asyncapi.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DepartmentEvent(
    val departmentId: UUID,
    val size: Long
) {
}