package ru.itmo.highload_systems.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import lombok.Builder
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
@Builder(toBuilder = true)
class OxygenStorage(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    var size: Long,
    var capacity: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    var department: Department,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime?=null
)