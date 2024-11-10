package ru.itmo.highload_systems.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.api.dto.RoomResponse
import ru.itmo.highload_systems.domain.mapper.OrderApiMapper
import ru.itmo.highload_systems.domain.mapper.RoomApiMapper
import ru.itmo.highload_systems.infra.repository.RoomRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class RoomService(
    private val roomRepository: RoomRepository,
    private val roomApiMapper: RoomApiMapper,
    private val orderApiMapper: OrderApiMapper
) {

    fun findById(id: UUID): RoomResponse {
        val room = roomRepository.findById(id)
            .orElseThrow { NoSuchElementException("Комнада с id %s не найдена") }
        return roomApiMapper.toDto(room)
    }

    fun findOrdersById(id: UUID): List<OrderResponse> {
        val room = roomRepository.findById(id)
            .orElseThrow { NoSuchElementException("Комнада с id %s не найдена") }
        return room.orders.map(orderApiMapper::toDto)
    }

    fun findAllByDepartmentId(departmentId: UUID, pageable: Pageable): Page<RoomResponse> {
        return roomRepository.findAllByDepartmentId(departmentId, pageable)
            .map(roomApiMapper::toDto)
    }
}