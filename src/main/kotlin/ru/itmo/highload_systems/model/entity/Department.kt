package ru.itmo.highload_systems.model.entity

import jakarta.persistence.*

@Entity
class Department(
    @Id @GeneratedValue
    val id: Long? = null,
    val name: String,
    val oxygenStoragesCount: Int,
    val workersCount: Int,
    @OneToMany
    val oxygenStorages: List<OxygenStorage>
)