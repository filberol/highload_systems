package ru.itmo.department.infra.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "department_user")
class DepartmentUser(
    @Id
    @jakarta.persistence.Id @GeneratedValue @UuidGenerator
    @Column("id")
    var id: UUID? = null,
    @Column("department_id")
    var departmentId: UUID? = null,
    @Column("user_id")
    var userId: UUID? = null,
    @Column("created_at")
    var createdAt: OffsetDateTime? = null,
    @Column("updated_at")
    var updatedAt: OffsetDateTime? = null
) {
    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        this.updatedAt = OffsetDateTime.now()
        this.createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
    }
}