package ru.itmo.oxygen.clients.dto

import java.time.OffsetDateTime
import java.util.*

data class RoomNormResponse(
    var id: UUID? = null,
    var peopleCount: Long? = null,
    var balanceOxygen: Long? = null,
    var avgPersonNorm: Long? = null,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
)