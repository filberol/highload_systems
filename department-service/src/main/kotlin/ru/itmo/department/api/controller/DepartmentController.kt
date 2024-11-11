package ru.itmo.department.api.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
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
    @GetMapping("/departments")
    fun getDepartments(
        @PageableDefault(
            sort = ["id"],
            size = 50
        ) pageable: Pageable
    ): Page<DepartmentResponse> = departmentService.getDepartments(pageable)


    @PostMapping("/departments/{id}/check-in")
    fun checkIn(
        @PathVariable id: UUID,
    ): CheckInResponse {
        return departmentService.checkIn(id)
    }

    @GetMapping("/departments/{id}/rooms")
    fun getRooms(
        @PathVariable id: UUID,
        @PageableDefault(sort = ["id"], size = 50) pageable: Pageable
    ): Page<RoomResponse> {
        return roomService.findAllByDepartmentId(id, pageable)
    }
}