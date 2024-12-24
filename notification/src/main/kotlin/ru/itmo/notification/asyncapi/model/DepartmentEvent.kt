package ru.itmo.notification.asyncapi.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DepartmentEvent(
    val departmentId: UUID? = null,
    val size: Long? = null
) {
}