package ru.itmo.highload_systems.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.*

@Entity
class Department(
    @Id
    @UuidGenerator
    val id: UUID? = null,
    val name: String,
    @OneToMany
    val oxygenStorages: List<OxygenStorage>,
    @OneToMany
    val rooms: List<Room>,
    val createdAt: OffsetDateTime
)