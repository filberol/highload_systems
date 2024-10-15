package ru.itmo.highload_systems.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class RoomNorm(
    @Id
    @UuidGenerator
    val id: UUID? = null,
    var size: Long,
    @ManyToOne
    val room: Room,
    var avgPersonNorm: Float,
    val createdAt: OffsetDateTime? = null
)