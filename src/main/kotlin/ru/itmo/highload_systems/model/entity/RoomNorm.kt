package ru.itmo.highload_systems.model.entity

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
    val size: Long,
    @ManyToOne
    val room: Room,
    val avgPersonNorm: Float,
    val createdAt: OffsetDateTime
)