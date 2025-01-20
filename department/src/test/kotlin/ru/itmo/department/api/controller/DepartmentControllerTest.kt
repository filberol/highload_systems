package ru.itmo.department.api.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import ru.itmo.department.api.dto.CheckInResponse
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.domain.service.DepartmentService
import ru.itmo.department.domain.service.RoomService
import ru.itmo.department.infra.model.enums.Role
import ru.itmo.department.security.WithMockUser
import java.time.OffsetDateTime
import java.util.*

class DepartmentControllerTest {

    private val departmentService = mockk<DepartmentService>()

    private val roomService = mockk<RoomService>()
    private val sut = DepartmentController(departmentService, roomService)


    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles = ["USER"])
    fun getDepartments_shouldInvokeService() {
        val expected = DepartmentResponse(
            id = UUID.randomUUID(),
            name = "department",
            createdAt = OffsetDateTime.now()
        )
        every {
            departmentService.getDepartments()
        }
            .returns(Flux.just(expected))
        // when
        val result = sut.getDepartments()

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()

        verify(exactly = 1) { departmentService.getDepartments() }
    }

    @Test
    @WithMockUser(role = Role.USER)
    fun getRooms_shouldInvokeService() {
        val id = UUID.randomUUID()
        val expected = RoomResponse(
            id = UUID.randomUUID(),
            departmentId = id,
            capacity = 5L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every {
            roomService.findAllByDepartmentId(
                id
            )
        }
            .returns(Flux.just(expected))
        // when
        val result = sut.getRooms(id)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()

        verify(exactly = 1) { roomService.findAllByDepartmentId(eq(id)) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
    fun checkIn_shouldInvokeService() {
        // given
        val departmentId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val roomId = UUID.randomUUID()
        val personCount = 5L
        val expected = CheckInResponse(
            departmentId = departmentId,
            roomId = roomId,
            personCount = personCount
        )
        every { departmentService.checkIn(departmentId, userId) }
            .returns(Mono.just(expected))
        // when
        val result = sut.checkIn(departmentId, userId)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
        verify(exactly = 1) { departmentService.checkIn(departmentId, userId) }
    }
}