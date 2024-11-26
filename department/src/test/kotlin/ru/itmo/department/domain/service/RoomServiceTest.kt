package ru.itmo.department.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import reactor.test.StepVerifier
import reactor.test.expectError
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.common.AbstractDatabaseTest
import ru.itmo.department.infra.model.Department
import ru.itmo.department.infra.model.Room
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
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )

        // when
        val result = sut.findById(roomId)

        // then
        StepVerifier.create(result).expectNextCount(1)
//        assertThat(result)
//            .usingComparatorForType(
//                OffsetDateTimeByInstantComparator.getInstance(),
//                OffsetDateTime::class.java
//            )
//            .usingRecursiveComparison()
//            .isEqualTo(expected)
    }

    @Test
    fun findById_shouldThrowException_whenNotExist() {
        val id = UUID.randomUUID()

        // when & then
        val result = sut.findById(id)
        StepVerifier.create(result).expectError(NoSuchElementException::class)

    }


    @Test
    fun findWithNormById_shouldInvokeRepository() {
        val roomId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = 1L,
            balanceOxygen = 10L,
            avgPersonNorm = 30 / 1,
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )

        // when
        val result = sut.findWithNormById(roomId)

        StepVerifier.create(result).expectNextCount(1)

        // then
//        assertThat(result)
//            .usingComparatorForType(
//                OffsetDateTimeByInstantComparator.getInstance(),
//                OffsetDateTime::class.java
//            )
//            .usingRecursiveComparison()
//            .isEqualTo(expected)
    }

    @Test
    fun findByDepartmentIdAndPersonOxygenNorm_shouldInvokeRepository() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val personOxygenNorm = 10L
        val department = Department(
            id = departmentId,
            name = "department",
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )
        val room = Room(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            department = department,
            capacity = 40L,
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )

        // when
        val result = sut.findByDepartmentIdAndPersonOxygenNorm(departmentId, personOxygenNorm)

        StepVerifier.create(result).expectNextCount(1)

        // then
//        assertThat(result)
//            .usingComparatorForType(
//                OffsetDateTimeByInstantComparator.getInstance(),
//                OffsetDateTime::class.java
//            )
//            .usingRecursiveComparison()
//            .ignoringFields("roomNorm", "department.rooms")
//            .isEqualTo(room)
    }

    @Test
    fun findByDepartmentIdAndPersonOxygenNorm_whenNotExist() {
        // given
        val departmentId = UUID.randomUUID()
        val personOxygenNorm = 10L

        // when & then

        val result = sut.findByDepartmentIdAndPersonOxygenNorm(
            departmentId,
            personOxygenNorm
        )

        StepVerifier.create(result).expectError(IllegalArgumentException::class)

    }

    @Test
    fun findAllByDepartmentId_shouldInvokeService() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val department = Department(
            id = departmentId,
            name = "department",
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )
        val room = Room(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            department = department,
            capacity = 40L,
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )

        val expected = RoomResponse(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            departmentId = department.id!!,
            capacity = room.capacity,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt
        )
        val result = sut.findAllByDepartmentId(departmentId, pageable)
        StepVerifier.create(result).expectNextCount(1)

//        assertThat(result)
//            .usingComparatorForType(
//                OffsetDateTimeByInstantComparator.getInstance(),
//                OffsetDateTime::class.java
//            )
//            .usingRecursiveComparison()
//            .isEqualTo(PageImpl(listOf(expected), pageable, 1))
    }

    @Test
    fun supplyOxygen_shouldInvokeService() {
        val departmentId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")
        val department = Department(
            id = departmentId,
            name = "department",
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )
        val room = Room(
            id = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5"),
            department = department,
            capacity = 40L,
            createdAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00"),
            updatedAt = OffsetDateTime.parse("2024-01-03T07:00:00.000000+00:00")
        )

        val expected = RoomNormResponse(
            id = room.id,
            peopleCount = 1L,
            balanceOxygen = 2L,
            avgPersonNorm = 38L,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt
        )

        // when
        val result = sut.supplyOxygen(room.id!!, 8L)

        StepVerifier.create(result).expectNextCount(1)

        // then
//        assertThat(result)
//            .usingComparatorForType(
//                OffsetDateTimeByInstantComparator.getInstance(),
//                OffsetDateTime::class.java
//            )
//            .usingRecursiveComparison()
//            .isEqualTo(expected)
    }


    @Test
    fun supplyOxygen_shouldThrowException() {
        val roomId = UUID.fromString("20006109-1144-4aa6-8fbf-f45435264de5")

        // when & then
        val result = sut.supplyOxygen(roomId, 11L)
        StepVerifier.create(result).expectError(IllegalArgumentException::class)

    }
}