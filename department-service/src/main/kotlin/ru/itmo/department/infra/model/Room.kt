package ru.itmo.department.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class Room(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    @OneToOne(mappedBy="room")
    var roomNorm: RoomNorm? = null,
    @ManyToOne
    var department: Department? = null,
    var capacity: Long? = null,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
)