package ru.itmo.auth.infra.model;

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import ru.itmo.auth.infra.model.enums.UserRole
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "users")
class User(
    @Id
    @UuidGenerator
    var id: UUID? = null,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole? = null,
    var name: String? = null,
    var login: String? = null,
    var password: String? = null,
    private var createdAt: OffsetDateTime? = null
) {

    @PrePersist
    @PreUpdate
    fun onSaveHook() {
        this.role = UserRole.USER
        this.createdAt = if (createdAt == null) OffsetDateTime.now() else createdAt
    }
}
