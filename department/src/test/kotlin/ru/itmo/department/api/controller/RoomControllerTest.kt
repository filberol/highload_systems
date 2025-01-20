package ru.itmo.department.api.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.domain.service.RoomService
import ru.itmo.department.infra.model.enums.Role
import ru.itmo.department.security.WithMockUser
import java.time.OffsetDateTime
import java.util.*

class RoomControllerTest {

    private val roomService = mockk<RoomService>()
    private val sut = RoomController(roomService)

    @Test
    @WithMockUser(role = Role.MANAGER)
    fun getRoomById_shouldInvokeServiceAndReturnResponse() {
        // given
        val roomId = UUID.randomUUID()
        val departmentId = UUID.randomUUID()
        val expected = RoomResponse(
            id = roomId,
            departmentId = departmentId,
            capacity = 5L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomService.findById(roomId) }.returns(Mono.just(expected))

        // when
        val result = sut.getRoomById(roomId)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()

        verify(exactly = 1) { roomService.findById(roomId) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
    fun getNormByRoomId_shouldInvokeServiceAndReturnResponse() {
        // given
        val roomId = UUID.randomUUID()
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = 5L,
            balanceOxygen = 10L,
            avgPersonNorm = 55,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomService.findWithNormById(roomId) }
            .returns(Mono.just(expected))

        // when
        val result = sut.getNormByRoomId(roomId)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
        verify(exactly = 1) { roomService.findWithNormById(roomId) }
    }

    @Test
    @WithMockUser(role = Role.ADMIN)
    fun supplyOxygen_shouldInvokeServiceAndReturnResponse() {
        // given
        val roomId = UUID.randomUUID()
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = 5L,
            balanceOxygen = 10L,
            avgPersonNorm = 55,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomService.supplyOxygen(roomId, eq(5L)) }
            .returns(Mono.just(expected))
        every { roomService.findAllByDepartmentId(any()) }
            .returns(Flux.empty())

        // when
        val result = sut.supplyOxygen(roomId, 5L)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()

        verify(exactly = 1) { roomService.supplyOxygen(roomId, 5L) }
    }
}