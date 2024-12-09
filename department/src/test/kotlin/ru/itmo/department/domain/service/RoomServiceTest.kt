package ru.itmo.department.domain.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import reactor.test.StepVerifier
import reactor.test.expectError
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.common.AbstractDatabaseTest
import java.time.OffsetDateTime
import java.util.*

class RoomServiceTest : AbstractDatabaseTest() {

    @Autowired
    private lateinit var sut: RoomService

    @Test
    fun findById_shouldInvokeRepository() {
        val roomId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val expected = RoomResponse(
            id = roomId,
            departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            capacity = 40L,
            createdAt = OffsetDateTime.parse("2024-01-03T10:00+03:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T10:00+03:00")
        )

        // when
        val result = sut.findById(roomId)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun findById_shouldThrowException_whenNotExist() {
        val id = UUID.randomUUID()

        // when & then
        val result = sut.findById(id)
        StepVerifier.create(result)
            .expectError(NoSuchElementException::class)
            .verify()
    }


    @Test
    fun findWithNormById_shouldInvokeRepository() {
        val roomId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = 1L,
            balanceOxygen = 10L,
            avgPersonNorm = 30 / 1,
            createdAt = OffsetDateTime.parse("2024-01-03T10:00+03:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T10:00+03:00")
        )

        // when
        val result = sut.findWithNormById(roomId)

        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun checkIn_shouldInvokeRepository() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val personOxygenNorm = 10L
        val expected = RoomNormResponse(
            id = departmentId,
            peopleCount = 2L,
            balanceOxygen = 10L,
            avgPersonNorm = 15L,
            createdAt = OffsetDateTime.parse("2024-01-03T10:00+03:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T10:00+03:00")
        )

        // when
        val result = sut.checkIn(departmentId, personOxygenNorm)

        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun findByDepartmentIdAndPersonOxygenNorm_whenNotExist() {
        // given
        val departmentId = UUID.randomUUID()
        val personOxygenNorm = 10L

        // when & then
        val result = sut.checkIn(
            departmentId,
            personOxygenNorm
        )
        StepVerifier.create(result).expectError(IllegalArgumentException::class)
            .verify()
    }

    @Test
    fun findAllByDepartmentId_shouldInvokeService() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )

        val expected = RoomResponse(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            departmentId = departmentId,
            capacity = 40L,
            createdAt = OffsetDateTime.parse("2024-01-03T10:00+03:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T10:00+03:00"),
        )
        val result = sut.findAllByDepartmentId(departmentId, pageable)
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
    }

    @Test
    fun supplyOxygen_shouldInvokeService() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")

        val expected = RoomNormResponse(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            peopleCount = 1L,
            balanceOxygen = 2L,
            avgPersonNorm = 38L,
            createdAt = OffsetDateTime.parse("2024-01-03T10:00+03:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T10:00+03:00")
        )

        // when
        val result = sut.supplyOxygen(departmentId, 8L)

        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
    }


    @Test
    fun supplyOxygen_shouldThrowException() {
        val roomId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")

        // when & then
        val result = sut.supplyOxygen(roomId, 11L)
        StepVerifier.create(result)
            .expectError(IllegalArgumentException::class)
            .verify()
    }
}