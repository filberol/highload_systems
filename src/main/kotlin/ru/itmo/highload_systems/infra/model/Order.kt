package ru.itmo.highload_systems.infra.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import ru.itmo.highload_systems.infra.model.enums.OrderStatus
import java.time.OffsetDateTime
import java.util.*

@Entity
class Order(
    @Id
    @UuidGenerator
    val id: UUID? = null,
    @Enumerated(EnumType.STRING)
    var status: OrderStatus,
    val size: Long,
    @ManyToOne
    var room: Room? = null,
    @ManyToOne
    val department: Department,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
)