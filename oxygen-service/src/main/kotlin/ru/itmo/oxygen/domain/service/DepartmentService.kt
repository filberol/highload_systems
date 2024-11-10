package ru.itmo.highload_systems.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.api.dto.DepartmentResponse
import ru.itmo.highload_systems.domain.mapper.DepartmentApiMapper
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.repository.DepartmentRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class DepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val departmentApiMapper: DepartmentApiMapper,
) {

    fun getDepartments(pageable: Pageable): Page<DepartmentResponse> {
        return departmentRepository.findAll(pageable).map(departmentApiMapper::toDto)
    }

    fun findById(id: UUID): Department {
        return departmentRepository.findById(id)
            .orElseThrow { NoSuchElementException("Департамент c id %s не найден".format(id)) }
    }
}