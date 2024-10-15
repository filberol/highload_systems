package ru.itmo.highload_systems.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.highload_systems.api.dto.DepartmentResponse
import ru.itmo.highload_systems.api.dto.RoomResponse
import ru.itmo.highload_systems.domain.mapper.DepartmentApiMapper
import ru.itmo.highload_systems.domain.mapper.RoomApiMapper
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.model.Room
import ru.itmo.highload_systems.infra.repository.DepartmentRepository
import java.util.UUID

@Service
@Transactional(readOnly = true)
class DepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val departmentApiMapper: DepartmentApiMapper
) {

    fun getDepartments(pageable: Pageable): Page<DepartmentResponse> {
        return departmentApiMapper.toDto(departmentRepository.findAll(pageable))
    }

    fun getRoomsById(id: UUID, pageable: Pageable): Page<RoomResponse> {
        val department = departmentRepository.findById(id)
    }

}