package ru.itmo.highload_systems.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.infra.model.Room
import ru.itmo.highload_systems.infra.repository.RoomNormRepository
import ru.itmo.highload_systems.infra.repository.RoomRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class RoomNormService(
    private val roomRepository: RoomRepository,
    private val roomNormRepository: RoomNormRepository
) {

    fun fillIfExistAndReturnRoom(departmentId: UUID, size: Long): Optional<Room> {
        val room = roomRepository.findFirstRoomByDepartmentAndSize(departmentId, size)
        if (room.isEmpty) {
            return room
        }
        val roomNorm = room.get().roomNorm
        roomNorm.size -= size
        roomNormRepository.save(roomNorm);
        return room
    }
}