package ru.itmo.highload_systems.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class OxygenStorage(
    @Id
    @UuidGenerator
    val id: UUID? = null,
    var size: Long,
    val capacity: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    val department: Department,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime?=null
)