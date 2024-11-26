package ru.itmo.order.infra.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import ru.itmo.order.infra.model.enums.OrderStatus
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "orders")
class Order(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    @Enumerated(EnumType.STRING)
    var status: OrderStatus? = null,
    var departmentId: UUID? = null,
    var userId: UUID? = null,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
) {

    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        this.status = if (status == null) OrderStatus.NEW else status
        this.updatedAt = OffsetDateTime.now()
        this.createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
    }
}