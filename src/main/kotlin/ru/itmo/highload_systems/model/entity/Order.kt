package ru.itmo.highload_systems.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import ru.itmo.highload_systems.model.enums.OrderStatus
import java.time.OffsetDateTime
import java.util.*

@Entity
class Order(
    @Id
    @UuidGenerator
    val id: UUID,
    @Enumerated(EnumType.STRING)
    val status: OrderStatus,
    val size: Long,
    @OneToOne
    val person: Person? = null,
    @ManyToOne
    val room: Room,
    @ManyToOne
    val department: Department,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)