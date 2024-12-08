package ru.itmo.department.infra.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.itmo.department.infra.model.RoomNorm
import java.util.*

@Repository
interface RoomNormRepository : ReactiveCrudRepository<RoomNorm, UUID> {

    fun findByRoomId(roomId: UUID): Mono<RoomNorm>
}