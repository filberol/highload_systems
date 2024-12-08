package ru.itmo.department.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.department.api.dto.CheckInResponse
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.clients.UserClient
import ru.itmo.department.domain.mapper.DepartmentApiMapper
import ru.itmo.department.infra.model.DepartmentUser
import ru.itmo.department.infra.repository.DepartmentRepository
import ru.itmo.department.infra.repository.DepartmentUserRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class DepartmentService(
    private val roomService: RoomService,
    private val userClient: UserClient,
    private val departmentApiMapper: DepartmentApiMapper,
    private val departmentRepository: DepartmentRepository,
    private val departmentUserRepository: DepartmentUserRepository
) {

    fun getDepartments(): Flux<DepartmentResponse> {
        return departmentRepository.findAll()
            .map(departmentApiMapper::toResponse)
    }

    @Transactional
    fun checkIn(id: UUID, userId: UUID): Mono<CheckInResponse> {
        return Mono.fromCallable {
            val response = userClient.getById(userId)
            if (!response.statusCode.is2xxSuccessful || response.body == null) {
                throw NoSuchElementException("Пользователь с id: $userId не найден");
            }
        }.flatMap { response ->
            roomService.checkIn(id, 10L)
        }.flatMap { room ->
            val departmentUser = DepartmentUser(
                userId = userId,
                departmentId = id
            )
            departmentUserRepository.save(departmentUser)
                .map {
                    // Transform to CheckInResponse
                    CheckInResponse(
                        departmentId = id,
                        roomId = room.id!!,
                        personCount = room.peopleCount!!
                    )
                }
        }
    }
}