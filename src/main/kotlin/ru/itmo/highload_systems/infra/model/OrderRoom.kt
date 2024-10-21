package ru.itmo.highload_systems.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import lombok.Builder
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
@Builder(toBuilder = true)
class OrderRoom(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    @ManyToOne
    var room: Room,
    @ManyToOne
    var order: Order,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
)
