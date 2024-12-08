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
@Table(name = "department")
class Department(
    @Id
    @jakarta.persistence.Id @GeneratedValue @UuidGenerator
    @Column("id")
    var id: UUID? = null,
    @Column("name")
    var name: String? = null,
    @Column("created_at")
    var createdAt: OffsetDateTime? = null
) {
    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        this.createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
    }
}