package ru.itmo.department.domain.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.domain.mapper.RoomApiMapper
import ru.itmo.department.domain.mapper.RoomNormApiMapper
import ru.itmo.department.infra.model.Room
import ru.itmo.department.infra.repository.RoomRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional(readOnly = true)
class RoomService(
    private val roomRepository: RoomRepository,
    private val roomNormService: RoomNormService,
    private val roomNormApiMapper: RoomNormApiMapper,
    private val roomApiMapper: RoomApiMapper
) {

    fun findById(id: UUID): Mono<RoomResponse> {
        return roomRepository.findById(id)
            .map(roomApiMapper::toResponse)
    }

    fun findByDepartmentIdAndPersonOxygenNorm(
        departmentId: UUID,
        personOxygenNorm: Long
    ): Mono<Room> {
        return roomRepository.findById(
            departmentId,
        ) }

    fun supplyOxygen(id: UUID, size: Long): Mono<RoomNormResponse> {
        return roomRepository.findById(id)
            .flatMap { room ->
                val roomNorm = room.roomNorm!!
                if (room.capacity!! - roomNorm.size!! < size) {
                    return@flatMap Mono.error<RoomNormResponse>(
                        IllegalArgumentException("В комнате с id $id недостаточно места для поставки воздуха размера $size")
                    )
                }
                roomNorm.size = roomNorm.size!! + size
                roomNormService.save(roomNorm).map { updatedRoomNorm ->
                    roomNormApiMapper.toResponse(room, updatedRoomNorm)
                }
            }
    }

    fun findWithNormById(id: UUID): Mono<RoomNormResponse> {
        return roomRepository.findById(id)
            .map { room -> roomNormApiMapper.toResponse(room, room.roomNorm!!) }
    }

    fun findAllByDepartmentId(departmentId: UUID, pageable: Pageable): Flux<RoomResponse> {
        return roomRepository.findAllByDepartmentId(departmentId, pageable)
            .map(roomApiMapper::toResponse)
    }
}