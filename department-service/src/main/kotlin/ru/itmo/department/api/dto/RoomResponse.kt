package ru.itmo.department.api.dto

import java.time.OffsetDateTime
import java.util.*

data class RoomResponse(
    var id: UUID? = null,
    var departmentId: UUID? = null,
    var capacity: Long? = null,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
)
