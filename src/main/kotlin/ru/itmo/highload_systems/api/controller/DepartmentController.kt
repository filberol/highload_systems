package ru.itmo.highload_systems.api.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.itmo.highload_systems.api.dto.DepartmentResponse
import ru.itmo.highload_systems.api.dto.OxygenStorageResponse
import ru.itmo.highload_systems.api.dto.RoomResponse
import ru.itmo.highload_systems.domain.service.DepartmentService
import ru.itmo.highload_systems.domain.service.OxygenStorageService
import ru.itmo.highload_systems.domain.service.RoomService
import java.util.*

@RestController
class DepartmentController(
    private val departmentService: DepartmentService,
    private val roomService: RoomService,
    private val storageService: OxygenStorageService
) {
    @GetMapping("/departments")
    fun getDepartments(
        pageable: Pageable,
    ): Page<DepartmentResponse> = departmentService.getDepartments(pageable)

    @GetMapping("/departments/{id}/rooms")
    fun getRooms(@PathVariable id: UUID, pageable: Pageable): Page<RoomResponse> {
        return roomService.findAllByDepartmentId(id, pageable)
    }

    @GetMapping("/departments/{id}/storage")
    fun getStorage(@PathVariable id: UUID): OxygenStorageResponse {
        return storageService.findByDepartmentId(id)
    }
}