package ru.itmo.department.domain.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.expectError
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.clients.UserClient
import ru.itmo.department.clients.dto.RoleRequestResponse
import ru.itmo.department.clients.dto.UserResponse
import ru.itmo.department.domain.mapper.DepartmentApiMapperImpl
import ru.itmo.department.infra.model.Department
import ru.itmo.department.infra.model.DepartmentUser
import ru.itmo.department.infra.repository.DepartmentRepository
import ru.itmo.department.infra.repository.DepartmentUserRepository
import java.time.OffsetDateTime
import java.util.*

class DepartmentServiceTest {

    private val roomService = mockk<RoomService>()
    private val userClient = mockk<UserClient>()
    private val departmentApiMapper = DepartmentApiMapperImpl()
    private val departmentRepository = mockk<DepartmentRepository>()
    private val departmentUserRepository = mockk<DepartmentUserRepository>()
    private val sut =
        DepartmentService(roomService, userClient, departmentApiMapper, departmentRepository, departmentUserRepository)


    @Test
    fun getDepartments_shouldInvokeRepository() {
        every { departmentRepository.findAll() }
            .returns(
                Flux.just(
                    Department(
                        id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
                        name = "department",
                        createdAt = OffsetDateTime.parse("2024-01-03T10:00+03:00")
                    )
                )
            )
        val department = DepartmentResponse(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            name = "department",
            createdAt = OffsetDateTime.parse("2024-01-03T10:00+03:00")
        )

        // when
        val result = sut.getDepartments()

        // then
        StepVerifier.create(result)
            .expectNext(department)
            .verifyComplete();
        verify { departmentRepository.findAll() }
    }

    @Test
    fun checkIn_shouldInvokeRepository() {
        // given
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val userId = UUID.randomUUID()
        val roomId = UUID.randomUUID()
        every { userClient.getById(any()) }
            .returns(UserResponse(userId, "name", "login", "password", RoleRequestResponse.USER))
        every { departmentUserRepository.findByUserId(any()) }
            .returns(Mono.empty())
        every { departmentUserRepository.save(any()) }
            .returns(Mono.just(DepartmentUser(id = UUID.randomUUID(), userId, departmentId)))
        every { roomService.checkIn(any(), any()) }
            .returns(Mono.just(RoomNormResponse(roomId, 2L, 2L, 50L, OffsetDateTime.now(), OffsetDateTime.now())))
        every { departmentRepository.save(any()) }
            .returns(
                Mono.just(
                    Department(
                        id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
                        name = "department",
                        createdAt = OffsetDateTime.parse("2024-01-03T10:00+03:00")
                    )
                )
            )
        // when
        val result = sut.checkIn(departmentId, userId)

        // then
        StepVerifier.create(result)
            .expectNextMatches { it.departmentId == departmentId }
            .verifyComplete()
        verify { userClient.getById(userId) }
        verify { roomService.checkIn(departmentId, 10L) }

    }

    @Test
    fun checkIn_shouldThrow_whenUserAlreadyCheckIn() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val userId = UUID.randomUUID()
        val roomId = UUID.randomUUID()
        every { userClient.getById(any()) }
            .returns(UserResponse(userId, "name", "login", "password", RoleRequestResponse.USER))
        every { departmentUserRepository.findByUserId(any()) }
            .returns(Mono.just(DepartmentUser(UUID.randomUUID(), userId, departmentId)))
        every { departmentUserRepository.save(any()) }
            .returns(Mono.just(DepartmentUser(id = UUID.randomUUID(), userId, departmentId)))
        every { roomService.checkIn(any(), any()) }
            .returns(Mono.just(RoomNormResponse(roomId, 2L, 2L, 50L, OffsetDateTime.now(), OffsetDateTime.now())))
        every { departmentRepository.save(any()) }
        // when
        val result = sut.checkIn(departmentId, userId)

        // then
        StepVerifier.create(result)
            .expectError(IllegalArgumentException::class)
            .verify()

        verify { userClient.getById(userId) }
    }
}