package ru.itmo.highload_systems.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class OxygenSupply(
    @Id @UuidGenerator
    val id: UUID? = null,
    val size: Long,
    @ManyToOne
    var oxygenStorage: OxygenStorage? = null,
    @ManyToOne
    val department: Department,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null
)
