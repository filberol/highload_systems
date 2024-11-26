package ru.itmo.department.infra.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "department_user")
class DepartmentUser(
    @UuidGenerator
    var id: UUID? = null,
    @OneToMany
    val department: Department? = null,
    var userId: UUID? = null,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
) {
    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        this.updatedAt = OffsetDateTime.now()
        this.createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
    }
}