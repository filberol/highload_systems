package ru.itmo.department.infra.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.department.infra.model.Room
import java.util.*

@Repository
interface RoomRepository : CrudRepository<Room, UUID> {

    @Query("SELECT r FROM Room r WHERE r.department.id = :departmentId AND (r.roomNorm.size/(r.roomNorm.peopleCount + 1))>=:personNorm")
    fun findFirstRoomByDepartmentIdAndPersonNorm(
        @Param("departmentId") departmentId: UUID,
        @Param("personNorm") personNorm: Long
    ): Optional<Room>

    fun findAllByDepartmentId(departmentId: UUID, pageable: Pageable): Page<Room>
}