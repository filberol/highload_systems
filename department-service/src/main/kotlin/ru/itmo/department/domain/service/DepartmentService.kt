package ru.itmo.department.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.department.api.config.DepartmentConfig.PERSON_OXYGEN_NORM
import ru.itmo.department.api.dto.CheckInResponse
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.domain.mapper.DepartmentApiMapper
import ru.itmo.department.infra.repository.DepartmentRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class DepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val departmentApiMapper: DepartmentApiMapper,
    private val roomService: RoomService,
    private val roomNormService: RoomNormService
) {

    fun getDepartments(pageable: Pageable): Page<DepartmentResponse> {
        return departmentRepository.findAll(pageable).map(departmentApiMapper::toResponse)
    }

    @Transactional(readOnly = false)
    fun checkIn(id: UUID): CheckInResponse {
        val room = roomService.findByDepartmentIdAndPersonOxygenNorm(id, PERSON_OXYGEN_NORM)
        val roomNorm = room.roomNorm
        roomNorm?.peopleCount = roomNorm?.peopleCount!! + 1
        roomNormService.save(roomNorm)
        return CheckInResponse(id, room.id!!, roomNorm.peopleCount!!)
    }
}