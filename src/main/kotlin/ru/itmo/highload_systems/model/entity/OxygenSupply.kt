package ru.itmo.highload_systems.model.entity

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
    val id: UUID,
    val size: Long,
    @ManyToOne
    val department: Department,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
