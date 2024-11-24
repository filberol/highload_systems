package ru.itmo.oxygen.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.oxygen.infra.model.OxygenSupply
import java.util.*

@Repository
interface OxygenSupplyRepository : JpaRepository<OxygenSupply, UUID>