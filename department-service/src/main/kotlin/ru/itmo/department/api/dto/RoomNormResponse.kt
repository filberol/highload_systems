package ru.itmo.department.api.dto

import java.time.OffsetDateTime
import java.util.*

data class RoomNormResponse(
    var id: UUID? = null,
    var peopleCount: Long? = null,
    var balanceOxygen: Long? = null,
    var avgPersonNorm: Double? = null,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
)