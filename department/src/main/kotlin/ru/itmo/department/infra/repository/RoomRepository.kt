package ru.itmo.department.infra.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.department.infra.model.Room
import java.util.*

@Repository
interface RoomRepository : ReactiveCrudRepository<Room, UUID> {

    fun findByDepartmentId(departmentId: UUID): Mono<Room>

    fun findAllByDepartmentId(departmentId: UUID, pageable: Pageable): Flux<Room>
}