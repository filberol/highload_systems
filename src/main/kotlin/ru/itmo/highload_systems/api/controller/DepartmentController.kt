package ru.itmo.highload_systems.api.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.itmo.highload_systems.api.dto.RoomListResponse
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.domain.service.DepartmentService
import java.util.UUID

@RestController
class DepartmentController(
    private val departmentService: DepartmentService
) {
//    @GetMapping("/departments")
//    fun getDepartments(
//        pageable: Pageable,
//        //TODO Implement mapping and return dto
//    ): Page<Department> = departmentService.getDepartments(pageable)
//
//    @GetMapping("/departments/{id}/rooms")
//    fun getRooms(@PathVariable id: UUID, pageable: Pageable): RoomListResponse {
//
//    }
}