package ru.itmo.highload_systems.model.entity

import jakarta.persistence.*

@Entity
class OxygenStorage(
    @Id @GeneratedValue
    val id: Long? = null,
    val filledPercent: Float,
    @ManyToOne(fetch = FetchType.LAZY)
    val department: Department
)