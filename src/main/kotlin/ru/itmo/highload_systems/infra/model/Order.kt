package ru.itmo.highload_systems.infra.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.mapstruct.Builder
import ru.itmo.highload_systems.infra.model.enums.OrderStatus
import java.time.OffsetDateTime
import java.util.*

@Entity
@lombok.Builder(toBuilder = true)
class Order(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    @Enumerated(EnumType.STRING)
    var status: OrderStatus,
    var size: Long,
    @ManyToOne
    var room: Room? = null,
    @ManyToOne
    var department: Department,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
)