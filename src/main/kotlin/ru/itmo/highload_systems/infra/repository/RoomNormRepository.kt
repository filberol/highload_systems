package ru.itmo.highload_systems.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.highload_systems.infra.model.RoomNorm
import java.util.*

interface RoomNormRepository : JpaRepository<RoomNorm, UUID>{

}