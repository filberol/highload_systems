package ru.itmo.highload_systems.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.infra.model.Room
import ru.itmo.highload_systems.infra.repository.RoomNormRepository
import ru.itmo.highload_systems.infra.repository.RoomRepository
import java.util.*

@Service
class RoomNormService(
    private val roomRepository: RoomRepository,
    private val roomNormRepository: RoomNormRepository
) {

    @Transactional(readOnly = false)
    fun fillIfExistAndReturnRoom(departmentId: UUID, size: Long): Optional<Room> {
        val room = roomRepository.findFirstRoomByDepartmentAndSize(departmentId, size)
        if (room.isEmpty) {
            return Optional.empty()
        }
        val roomNorm = room.get().roomNorm
        if (roomNorm != null) {
            roomNorm.size -= size
            roomNormRepository.save(roomNorm);
            return room
        }
        return Optional.empty()
    }
}