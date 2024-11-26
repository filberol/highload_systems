package ru.itmo.department.api.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.domain.service.RoomService
import java.util.*

@RestController
class RoomController(
    private val roomService: RoomService
) {
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @GetMapping("/rooms/{id}")
    fun getRoomById(@PathVariable id: UUID): Mono<RoomResponse> {
        return roomService.findById(id)
    }

    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'ADMIN')")
    @PostMapping("/rooms/{id}/supply-oxygen")
    fun supplyOxygen(
        @PathVariable id: UUID,
        @RequestParam("size") size: Long
    ): Mono<RoomNormResponse> {
        return roomService.supplyOxygen(id, size)
    }

    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @GetMapping("/rooms/{id}/norm")
    fun getNormByRoomId(@PathVariable id: UUID): Mono<RoomNormResponse> {
        return roomService.findWithNormById(id)
    }
}