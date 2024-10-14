package ru.itmo.highload_systems.domain.service

import org.springframework.stereotype.Service
import ru.itmo.highload_systems.infra.model.Room
import ru.itmo.highload_systems.infra.repository.RoomRepository
import java.util.Optional
import java.util.UUID

@Service
class RoomService(
    private val roomRepository: RoomRepository
) {

    fun findFreeByDepartmentIdAndSize(departmentId: UUID, size: Long): Optional<Room> {
        return roomRepository.findFirstRoomByDepartmentAndSize(departmentId, size)
    }

}