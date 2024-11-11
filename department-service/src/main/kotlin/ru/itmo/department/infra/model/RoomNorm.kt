package ru.itmo.department.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
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
)
