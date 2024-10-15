package ru.itmo.highload_systems.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.itmo.highload_systems.infra.model.OxygenSupply
import java.util.*

interface OxygenSupplyRepository : JpaRepository<OxygenSupply, UUID> {


}