package ru.itmo.highload_systems.infra.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.highload_systems.infra.model.Room
import java.util.*

@Repository
interface RoomRepository : CrudRepository<Room, UUID> {

    @Query("SELECT r FROM Room r WHERE r.department.id = :departmentId AND (r.capacity - r.roomNorm.size) >= :size ORDER BY r.createdAt ASC")
    fun findFirstRoomByDepartmentAndSize(
        @Param("departmentId") departmentId: UUID,
        @Param("size") minValue: Long
    ): Optional<Room>

    fun findAllByDepartmentId(departmentId: UUID, pageable: Pageable): Page<Room>
}