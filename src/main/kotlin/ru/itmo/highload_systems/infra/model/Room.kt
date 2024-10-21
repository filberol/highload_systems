package ru.itmo.highload_systems.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import lombok.Builder
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.List

@Entity
@Builder(toBuilder = true)
class Room(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    var number: Long,
    var capacity: Long,
    @OneToMany
    var orders: List<Order>,
    @OneToOne
    var roomNorm: RoomNorm,
    @ManyToOne
    var department: Department,
    var createdAt: OffsetDateTime,
    var updatedAt: OffsetDateTime
)