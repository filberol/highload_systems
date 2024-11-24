package ru.itmo.department.infra.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class RoomNorm(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    @OneToOne
    var room: Room? = null,
    var size: Long? = null,
    var peopleCount: Long? = null,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
) {
    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        this.updatedAt = OffsetDateTime.now()
        this.createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
    }
}
