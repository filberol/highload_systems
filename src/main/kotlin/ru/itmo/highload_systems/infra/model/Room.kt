package ru.itmo.highload_systems.infra.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class Room(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    var number: Long,
    var capacity: Long,
    @OneToMany
    var orders: List<Order>,
    @OneToOne
    var roomNorm: RoomNorm? = null,
    @ManyToOne
    var department: Department,
    var createdAt: OffsetDateTime,
    var updatedAt: OffsetDateTime
)