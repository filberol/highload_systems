package ru.itmo.department.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.domain.mapper.RoomApiMapper
import ru.itmo.department.domain.mapper.RoomNormApiMapper
import ru.itmo.department.infra.model.Room
import ru.itmo.department.infra.repository.RoomRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class RoomService(
    private val roomRepository: RoomRepository,
    private val roomNormService: RoomNormService,
    private val roomNormApiMapper: RoomNormApiMapper,
    private val roomApiMapper: RoomApiMapper
) {

    fun findById(id: UUID): RoomResponse {
        val room = findByRoomId(id)
        return roomApiMapper.toResponse(room)
    }

    fun findByDepartmentIdAndPersonOxygenNorm(
        departmentId: UUID,
        personOxygenNorm: Long
    ): Room {
        val room =
            roomRepository.findFirstRoomByDepartmentIdAndPersonNorm(departmentId, personOxygenNorm)
                .orElseThrow {
                    IllegalArgumentException(
                        "В департаменте с id %s нет свободных комнат".format(
                            departmentId
                        )
                    )
                }
        return room
    }

    fun supplyOxygen(id: UUID, size: Long): RoomNormResponse {
        val room = findByRoomId(id)
        val roomNorm = room.roomNorm!!
        if (room.capacity!! - roomNorm.size!! < size) {
            throw IllegalArgumentException(
                "В комнате с id %s недостаточно места для поставки воздуха размера %s"
                    .format(id, size)
            )
        }
        roomNorm.size = roomNorm.size!! + size
        roomNormService.save(roomNorm)
        return roomNormApiMapper.toResponse(room, roomNorm)
    }

    fun findWithNormById(id: UUID): RoomNormResponse {
        val room = findByRoomId(id)
        return roomNormApiMapper.toResponse(room, room.roomNorm!!)
    }

    fun findAllByDepartmentId(departmentId: UUID, pageable: Pageable): Page<RoomResponse> {
        return roomRepository.findAllByDepartmentId(departmentId, pageable)
            .map(roomApiMapper::toResponse)
    }

    private fun findByRoomId(id: UUID): Room {
        return roomRepository.findById(id)
            .orElseThrow { NoSuchElementException("Комната с id %s не найдена") }
    }
}