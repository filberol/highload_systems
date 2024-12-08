package ru.itmo.oxygen.infra.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "oxygen_storage")
class OxygenStorage(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    @Column(name = "size")
    var size: Long? = null,
    @Column(name = "created_at")
    var createdAt: OffsetDateTime? = null,
    @Column(name = "updated_at")
    var updatedAt: OffsetDateTime? = null
) {

    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        this.updatedAt = OffsetDateTime.now()
        this.createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
    }
}