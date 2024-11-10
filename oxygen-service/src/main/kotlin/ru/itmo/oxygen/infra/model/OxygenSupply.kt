package ru.itmo.highload_systems.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class OxygenSupply(
    @Id @UuidGenerator
    var id: UUID? = null,
    var size: Long,
    @ManyToOne
    var oxygenStorage: OxygenStorage? = null,
    @ManyToOne
    var department: Department,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
)
