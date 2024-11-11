package ru.itmo.user.model.entity

import jakarta.persistence.*
import ru.itmo.user.model.enums.Role

@Entity
@Table(name = "roles")
data class RoleEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    val role: Role? = null
)