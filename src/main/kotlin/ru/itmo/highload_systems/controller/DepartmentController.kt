package ru.itmo.highload_systems.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.highload_systems.model.entity.Department
import ru.itmo.highload_systems.service.DepartmentService

@RestController
class DepartmentController(
    private val departmentService: DepartmentService
) {
    @GetMapping("/departments")
    fun getDepartments(
        pageable: Pageable,
        //TODO Implement mapping and return dto
    ): Page<Department> = departmentService.getDepartments(pageable)
}