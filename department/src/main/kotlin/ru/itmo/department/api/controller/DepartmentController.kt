package ru.itmo.department.api.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.department.api.dto.CheckInResponse
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.domain.service.DepartmentService
import ru.itmo.department.domain.service.RoomService
import java.util.*

@RestController
class DepartmentController(
    private val departmentService: DepartmentService,
    private val roomService: RoomService
) {
    @PreAuthorize("permitAll()")
    @GetMapping("/departments")
    fun getDepartments(): Flux<DepartmentResponse> = departmentService.getDepartments()


    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    @PostMapping("/departments/{id}/check-in")
    fun checkIn(
        @PathVariable id: UUID,
        @RequestParam userId: UUID
    ): Mono<CheckInResponse> {
        return departmentService.checkIn(id, userId)
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/departments/{id}/rooms")
    fun getRooms(
        @PathVariable id: UUID,
        @PageableDefault(sort = ["id"], size = 50) pageable: Pageable
    ): Flux<RoomResponse> {
        return roomService.findAllByDepartmentId(id, pageable)
    }
}