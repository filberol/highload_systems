package ru.itmo.highload_systems.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class OrderRoom(
    @Id
    @UuidGenerator
    val id: UUID? = null,
    @ManyToOne
    val room: Room,
    @ManyToOne
    val order: Order,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null
)
