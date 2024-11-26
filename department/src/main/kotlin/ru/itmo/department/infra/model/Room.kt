package ru.itmo.department.infra.model

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "room")
class Room(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    @OneToOne(mappedBy = "room")
    var roomNorm: RoomNorm? = null,
    @ManyToOne
    var department: Department? = null,
    @Column("capacity")
    var capacity: Long? = null,
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