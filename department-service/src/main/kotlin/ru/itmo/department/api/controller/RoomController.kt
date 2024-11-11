package ru.itmo.department.api.controller

import org.springframework.web.bind.annotation.*
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.domain.service.RoomService
import java.util.*

@RestController
class RoomController(
    private val roomService: RoomService
) {
    @GetMapping("/rooms/{id}")
    fun getRoomById(@PathVariable id: UUID): RoomResponse {
        return roomService.findById(id)
    }

    @PostMapping("/rooms/{id}/supply-oxygen")
    fun supplyOxygen(
        @PathVariable id: UUID,
        @RequestParam("size") size: Long
    ): RoomNormResponse {
        return roomService.supplyOxygen(id, size)
    }

    @GetMapping("/rooms/{id}/norm")
    fun getNormByRoomId(@PathVariable id: UUID): RoomNormResponse {
        return roomService.findWithNormById(id)
    }
}