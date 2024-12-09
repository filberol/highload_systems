package ru.itmo.department.domain.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.domain.mapper.RoomApiMapper
import ru.itmo.department.infra.repository.RoomNormRepository
import ru.itmo.department.infra.repository.RoomRepository
import java.util.*

@Service
class RoomService(
    private val roomRepository: RoomRepository,
    private val roomNormRepository: RoomNormRepository,
    private val roomApiMapper: RoomApiMapper
) {

    @Transactional(readOnly = true)
    fun findById(id: UUID): Mono<RoomResponse> {
        return roomRepository.findById(id)
            .switchIfEmpty(Mono.error(NoSuchElementException("Комната с id %s не найдена")))
            .map { room -> roomApiMapper.toResponse(room) }
    }

    fun checkIn(
        departmentId: UUID,
        personOxygenNorm: Long
    ): Mono<RoomNormResponse> {
        return roomRepository.findByDepartmentIdAndPersonNorm(departmentId, personOxygenNorm)
            .switchIfEmpty(Mono.error(IllegalArgumentException("В департаменте с id $departmentId нет доступных комнат для поставки кислорода")))
            .publishOn(Schedulers.boundedElastic())
            .map { room ->
                val roomNorm = roomNormRepository.findByRoomId(room.id!!).block()
                roomNorm!!.peopleCount = roomNorm.peopleCount + 1
                roomNormRepository.save(roomNorm).block()
                roomApiMapper.toResponse(room, roomNorm)
            }
    }

    @Transactional
    fun supplyOxygen(id: UUID, size: Long): Mono<RoomNormResponse> {
        return roomRepository.findByDepartmentIdAndAvailableOxygenSize(id, size)
            .switchIfEmpty(Mono.error(IllegalArgumentException("В департаменте с id $id нет доступных комнат для поставки кислорода")))
            .publishOn(Schedulers.boundedElastic())
            .map { room ->
                val roomNorm = roomNormRepository.findByRoomId(room!!.id!!).block()
                roomNorm!!.size = roomNorm.size + size
                roomNormRepository.save(roomNorm).block()
                roomApiMapper.toResponse(room, roomNorm)
            }
    }

    @Transactional(readOnly = true)
    fun findWithNormById(id: UUID): Mono<RoomNormResponse> {
        return roomRepository.findById(id)
            .flatMap { room ->
                roomNormRepository.findByRoomId(id)
                    .map { roomNorm ->
                        roomApiMapper.toResponse(room, roomNorm)
                    }
            }
    }

    @Transactional(readOnly = true)
    fun findAllByDepartmentId(departmentId: UUID, pageable: Pageable): Flux<RoomResponse> {
        return roomRepository.findByDepartmentId(departmentId, pageable)
            .map(roomApiMapper::toResponse)
    }
}