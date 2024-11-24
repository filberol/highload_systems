package ru.itmo.department.infra.model

import jakarta.persistence.*
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
    @JoinColumn(name = "department_id")
    var rooms: List<Room>? = null,
    var createdAt: OffsetDateTime? = null
) {
    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        this.createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
    }
}