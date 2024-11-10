package ru.itmo.highload_systems.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.highload_systems.infra.model.OxygenSupply
import java.util.*

@Repository
interface OxygenSupplyRepository : JpaRepository<OxygenSupply, UUID>