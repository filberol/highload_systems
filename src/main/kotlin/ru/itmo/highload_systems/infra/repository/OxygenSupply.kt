package ru.itmo.highload_systems.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.highload_systems.infra.model.OxygenSupply
import java.util.*

interface OxygenSupply : JpaRepository<OxygenSupply, UUID>