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

    @Query("select * from room join room_norm on room.id=room_norm.room_id where room_norm.size/(room_norm.people_count+1) > :#{[1]} and room.department_id = :#{[0]}")
    fun findByDepartmentIdAndPersonNorm(
        @Param("departmentId") departmentId: UUID,
        @Param("personNorm") personNorm: Long
    ): Mono<Room>


    @Query("select * from room join room_norm on room.id=room_norm.room_id where room.department_id = :#{[0]} and room.capacity - room_norm.size >= :#{[1]}")
    fun findByDepartmentIdAndAvailableOxygenSize(
        @Param("departmentId") departmentId: UUID,
        @Param("size") size: Long
    ): Mono<Room>

    fun findByDepartmentId(departmentId: UUID, pageable: Pageable): Flux<Room>

}