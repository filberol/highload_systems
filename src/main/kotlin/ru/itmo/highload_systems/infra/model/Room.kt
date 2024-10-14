package ru.itmo.highload_systems.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.List

@Entity
class Room(
    @Id
    @UuidGenerator
    val id: UUID? = null,
    val number: Long,
    val capacity: Long,
    @OneToMany
    val orders: List<Order>,
    @OneToOne
    val roomNorm: RoomNorm,
    @ManyToOne
    val department: Department,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)