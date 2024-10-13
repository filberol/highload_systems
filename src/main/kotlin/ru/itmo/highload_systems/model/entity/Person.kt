package ru.itmo.highload_systems.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity
class Person(
    @Id
    @UuidGenerator
    val id: UUID? = null,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val isAlive: Boolean
)
