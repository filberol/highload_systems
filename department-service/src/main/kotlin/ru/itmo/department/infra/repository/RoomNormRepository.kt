package ru.itmo.department.infra.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.department.infra.model.RoomNorm
import java.util.*

@Repository
interface RoomNormRepository : JpaRepository<RoomNorm, UUID>