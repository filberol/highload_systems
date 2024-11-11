package ru.itmo.department.domain.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.common.config.DomainMapperTestConfiguration
import ru.itmo.department.infra.model.Department
import ru.itmo.department.infra.model.Room
import ru.itmo.department.infra.model.RoomNorm
import ru.itmo.department.infra.repository.RoomRepository
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [RoomService::class])
@Import(value = [DomainMapperTestConfiguration::class, RoomServiceTest.RoomServiceTestConfig::class])
class RoomServiceTest {

    @TestConfiguration
    internal class RoomServiceTestConfig {
        @Bean
        fun roomRepository() = mockk<RoomRepository>()

        @Bean
        fun roomNormService() = mockk<RoomNormService>()
    }

    @Autowired
    private lateinit var sut: RoomService
    @Autowired
    private lateinit var roomRepository: RoomRepository
    @Autowired
    private lateinit var roomNormService: RoomNormService


    @Test
    fun findById_shouldInvokeRepository() {
        val roomId = UUID.randomUUID()
        val departmentId = UUID.randomUUID()
        val department = Department(
            id = departmentId
        )
        val room = Room(
            id = roomId,
            department = department,
            capacity = 3L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomRepository.findById(roomId) }
            .returns(Optional.of(room))
        val expected = RoomResponse(
            id = roomId,
            departmentId = departmentId,
            capacity = room.capacity,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt
        )

        // when
        val result = sut.findById(roomId)

        // then
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify { roomRepository.findById(roomId) }
    }

    @Test
    fun findById_shouldThrowException_whenNotExist() {
        val id = UUID.randomUUID()
        every { roomRepository.findById(id) }.returns(Optional.empty())

        // when & then
        assertThrows<NoSuchElementException> { sut.findById(id) }
        verify { roomRepository.findById(id) }
    }


    @Test
    fun findWithNormById_shouldInvokeRepository() {
        val roomId = UUID.randomUUID()
        val departmentId = UUID.randomUUID()
        val roomNormId = UUID.randomUUID()
        val department = Department(
            id = departmentId
        )
        val roomNorm = RoomNorm(
            id = roomNormId,
            size = 10L,
            peopleCount = 5L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val room = Room(
            id = roomId,
            department = department,
            capacity = 3L,
            roomNorm = roomNorm,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomRepository.findById(roomId) }
            .returns(Optional.of(room))
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = roomNorm.peopleCount,
            balanceOxygen = room.capacity!! - roomNorm.size!!,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt
        )

        // when
        val result = sut.findWithNormById(roomId)

        // then
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify { roomRepository.findById(roomId) }
    }

    @Test
    fun findByDepartmentIdAndPersonOxygenNorm_shouldInvokeRepository() {
        val roomId = UUID.randomUUID()
        val departmentId = UUID.randomUUID()
        val personOxygenNorm = 10L
        val department = Department(
            id = departmentId
        )
        val room = Room(
            id = roomId,
            department = department,
            capacity = 3L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every {
            roomRepository.findFirstRoomByDepartmentIdAndPersonNorm(
                departmentId,
                personOxygenNorm
            )
        }
            .returns(Optional.of(room))

        // when
        val result = sut.findByDepartmentIdAndPersonOxygenNorm(departmentId, personOxygenNorm)

        // then
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(room)
        verify {
            roomRepository.findFirstRoomByDepartmentIdAndPersonNorm(
                departmentId,
                personOxygenNorm
            )
        }
    }

    @Test
    fun findByDepartmentIdAndPersonOxygenNorm_whenNotExist() {
        // given
        val departmentId = UUID.randomUUID()
        val personOxygenNorm = 10L
        every {
            roomRepository.findFirstRoomByDepartmentIdAndPersonNorm(
                departmentId,
                personOxygenNorm
            )
        }
            .returns(Optional.empty())

        // when
        assertThrows<IllegalArgumentException> {
            sut.findByDepartmentIdAndPersonOxygenNorm(
                departmentId,
                personOxygenNorm
            )
        }

        // then
        verify {
            roomRepository.findFirstRoomByDepartmentIdAndPersonNorm(
                departmentId,
                personOxygenNorm
            )
        }
    }

    @Test
    fun findAllByDepartmentId_shouldInvokeService() {
        val id = UUID.randomUUID()
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val room = Room(
            id = id,
            capacity = 3L,
            department = department,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )
        val page: Page<Room> = PageImpl(listOf(room), pageable, 1)
        every { roomRepository.findAllByDepartmentId(id, pageable) }
            .returns(page)
        val expected = RoomResponse(
            id = id,
            departmentId = department.id!!,
            capacity = room.capacity,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt
        )
        val result = sut.findAllByDepartmentId(id, pageable)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(PageImpl(listOf(expected), pageable, 1))
    }

    @Test
    fun supplyOxygen_shouldInvokeService() {
        val roomId = UUID.randomUUID()
        val roomNormId = UUID.randomUUID()
        val size = 5L
        val roomNorm = RoomNorm(
            id = roomNormId,
            size = 3L,
            peopleCount = 5L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val room = Room(
            id = roomId,
            roomNorm = roomNorm,
            capacity = 10L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomRepository.findById(roomId) }
            .returns(Optional.of(room))
        val updatedRoomNorm = RoomNorm(
            id = roomNormId,
            size = 8L,
            peopleCount = 5L,
            createdAt = roomNorm.createdAt,
            updatedAt = roomNorm.updatedAt
        )
        every { roomNormService.save(any()) }
            .returns(updatedRoomNorm)
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = updatedRoomNorm.peopleCount,
            balanceOxygen = 2L,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt
        )

        // when
        val result = sut.supplyOxygen(roomId, size)

        // then
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify { roomRepository.findById(roomId) }
    }


    @Test
    fun supplyOxygen_shouldThrowException() {
        val roomId = UUID.randomUUID()
        val roomNormId = UUID.randomUUID()
        val size = 5L
        val roomNorm = RoomNorm(
            id = roomNormId,
            size = 6L,
            peopleCount = 5L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val room = Room(
            id = roomId,
            roomNorm = roomNorm,
            capacity = 10L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomRepository.findById(roomId) }
            .returns(Optional.of(room))
        val updatedRoomNorm = RoomNorm(
            id = roomNormId,
            size = 8L,
            peopleCount = 5L,
            createdAt = roomNorm.createdAt,
            updatedAt = roomNorm.updatedAt
        )
        every { roomNormService.save(any()) }
            .returns(updatedRoomNorm)

        // when
        assertThrows<IllegalArgumentException> { sut.supplyOxygen(roomId, size) }

        // then
        verify { roomRepository.findById(roomId) }
        verify { roomNormService.save(any()) }
    }
}