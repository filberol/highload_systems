package ru.itmo.department.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.department.api.dto.CheckInResponse
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.config.DepartmentConfig.PERSON_OXYGEN_NORM
import ru.itmo.department.domain.mapper.DepartmentApiMapper
import ru.itmo.department.infra.repository.DepartmentRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class DepartmentService(
    private val departmentApiMapper: DepartmentApiMapper,
    private val roomService: RoomService,
    private val roomNormService: RoomNormService,
    private val departmentRepository: DepartmentRepository
) {

    fun getDepartments(): Flux<DepartmentResponse> {
        return departmentRepository.findAll()
            .map(departmentApiMapper::toResponse)
    }

    @Transactional
    fun checkIn(id: UUID, userId: UUID): Mono<CheckInResponse> {
        return roomService.findByDepartmentIdAndPersonOxygenNorm(id, 0L)
            .switchIfEmpty(
                Mono.error(IllegalArgumentException("Сейчас нет свободных департаментов для заселения"))
            )
            .flatMap { room ->
                val roomNorm = room.roomNorm
                    ?: return@flatMap Mono.error<CheckInResponse>(
                        IllegalArgumentException("Комната не имеет RoomNorm")
                    )

                // Увеличиваем количество людей в комнате
                roomNorm.peopleCount = (roomNorm.peopleCount ?: 0) + 1

                val department = room.department
                    ?: return@flatMap Mono.error<CheckInResponse>(
                        IllegalArgumentException("Комната не связана с департаментом")
                    )

                // Сохраняем изменения в департаменте и RoomNorm
                departmentRepository.save(department.addUser(userId))
                    .then(roomNormService.save(roomNorm))
                    .map {
                        CheckInResponse(departmentId = id, roomId = room.id!!, personCount = roomNorm.peopleCount!!)
                    }
            }
            .onErrorResume { error ->
                println(error.message)
                Mono.error(IllegalArgumentException("Сейчас нет свободных департаментов для заселения"))
            }
    }

}