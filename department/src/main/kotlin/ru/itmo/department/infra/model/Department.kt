package ru.itmo.department.infra.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.ArrayList

@Entity
@Table(name = "department")
class Department(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    var name: String? = null,
    @OneToMany
    @JoinColumn(name = "department_id")
    var rooms: List<Room>? = null,
    @OneToMany
    @JoinColumn(name = "department_id")
    var users: java.util.ArrayList<DepartmentUser> = ArrayList(),
    var createdAt: OffsetDateTime? = null
) {
    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        this.createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
    }

    fun addUser(userId: UUID): Department {
        this.users.add(DepartmentUser(department = this, userId = userId))
        return this
    }
}