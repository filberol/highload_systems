package ru.itmo.department.infra.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.department.infra.model.Room
import java.util.*

@Repository
interface RoomRepository : ReactiveCrudRepository<Room, UUID> {

    @Query("""
    select r
    from room r
         join room_norm rn ON r.id = rn.room_id
    WHERE r.department_id = :departmentId
      AND (rn.size / (rn.people_count + 1)) >= :personNorm
    """)
    fun findRoomByDepartmentIdAndPersonNorm(
        @Param("departmentId") departmentId: UUID,
        @Param("personNorm") personNorm: Long
    ): Flux<Room>

    fun findAllByDepartmentId(departmentId: UUID, pageable: Pageable): Flux<Room>
}