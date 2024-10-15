package ru.itmo.highload_systems.api.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.api.dto.RoomResponse
import ru.itmo.highload_systems.domain.service.RoomService
import java.util.*

@RestController
class RoomController(
    private val roomService: RoomService
) {
    @GetMapping("/rooms/{id}")
    fun getRoomById(@PathVariable id: UUID): RoomResponse {
        return roomService.findById(id)
    }

    @GetMapping("/rooms/{id}/orders")
    fun getOrdersByRoomId(@PathVariable id: UUID): List<OrderResponse> {
        return roomService.findOrdersById(id);
    }
}