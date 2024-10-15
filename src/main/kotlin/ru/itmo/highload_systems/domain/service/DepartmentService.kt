package ru.itmo.highload_systems.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.repository.DepartmentRepository
import java.util.UUID

@Service
@Transactional(readOnly = true)
class DepartmentService(
    private val departmentRepository: DepartmentRepository
) {

    fun getDepartments(pageable: Pageable): Page<Department> =
        departmentRepository.findAll(pageable)

}