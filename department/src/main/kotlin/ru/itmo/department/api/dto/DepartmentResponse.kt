package ru.itmo.department.api.dto

import java.time.OffsetDateTime
import java.util.*

data class DepartmentResponse(
    var id: UUID? = null,
    var name: String? = null,
    var createdAt: OffsetDateTime? = null
)
