package ru.itmo.department.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class Department(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    var name: String? = null,
    @OneToMany
    var rooms: List<Room>? = null,
    var createdAt: OffsetDateTime? = null
)