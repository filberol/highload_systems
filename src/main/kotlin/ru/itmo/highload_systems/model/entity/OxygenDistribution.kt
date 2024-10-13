package ru.itmo.highload_systems.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
class OxygenDistribution(
    @Id
    @UuidGenerator
    private val id: UUID,
    private val size: Long,
    @ManyToOne
    private val department: Department,
    @ManyToOne
    private val storage: OxygenStorage
)