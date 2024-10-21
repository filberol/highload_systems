package ru.itmo.highload_systems.domain.service

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
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.api.dto.OrderStatusRequestResponse
import ru.itmo.highload_systems.api.dto.RoomResponse
import ru.itmo.highload_systems.common.config.DomainMapperTestConfiguration
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.model.Order
import ru.itmo.highload_systems.infra.model.Room
import ru.itmo.highload_systems.infra.model.RoomNorm
import ru.itmo.highload_systems.infra.model.enums.OrderStatus
import ru.itmo.highload_systems.infra.repository.RoomRepository
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [RoomService::class])
@Import(value = [DomainMapperTestConfiguration::class, RoomServiceTest.RoomServiceTestConfig::class])
class RoomServiceTest {

    @TestConfiguration
    internal class RoomServiceTestConfig {
        @Bean
        fun roomRepository() = mockk<RoomRepository>()
    }

    @Autowired
    private lateinit var sut: RoomService

    @Autowired
    private lateinit var roomRepository: RoomRepository


    @Test
    fun findById_shouldInvokeRepository() {
        val id = UUID.randomUUID()
        val roomNorm = RoomNorm(
            id = UUID.randomUUID(),
            size = 6L,
            avgPersonNorm = 53.1F,
            createdAt = OffsetDateTime.now()
        )
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val room = Room(
            id = id,
            number = 1L,
            capacity = 3L,
            orders = emptyList(),
            roomNorm = roomNorm,
            department = department,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomRepository.findById(id) }
            .returns(Optional.of(room))

        // var
        val result = sut.findById(id)
        val expected = RoomResponse(
            id = id,
            number = room.number,
            size = roomNorm.size,
            capacity = room.capacity,
            avgPersonNorm = roomNorm.avgPersonNorm,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt
        )
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify { roomRepository.findById(id) }
    }

    @Test
    fun findById_shouldThrowException_whenNotExist() {
        val id = UUID.randomUUID()
        every { roomRepository.findById(id) }.returns(Optional.empty())
        assertThrows<NoSuchElementException> { sut.findById(id) }
        verify { roomRepository.findById(id) }
    }

    @Test
    fun findOrdersById_shouldInvokeRepository() {
        val id = UUID.randomUUID()
        val roomNorm = RoomNorm(
            id = UUID.randomUUID(),
            size = 6L,
            avgPersonNorm = 53.1F,
            createdAt = OffsetDateTime.now()
        )
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val room = Room(
            id = id,
            number = 1L,
            capacity = 3L,
            orders = emptyList(),
            roomNorm = roomNorm,
            department = department,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val order = Order(
            id = UUID.randomUUID(),
            status = OrderStatus.NEW,
            size = 5L,
            room = room,
            department = department,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        room.orders = listOf(order)
        every { roomRepository.findById(id) }
            .returns(Optional.of(room))

        // var
        val result = sut.findOrdersById(id)
        val expected = OrderResponse(
            id = order.id!!,
            status = OrderStatusRequestResponse.NEW,
            size = order.size,
            departmentId = department.id!!,
            createdAt = order.createdAt!!,
            updatedAt = order.updatedAt!!
        )
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(listOf(expected))
        verify { roomRepository.findById(id) }
    }

    @Test
    fun findOrdersById_shouldThrowException_whenNotExist() {
        val id = UUID.randomUUID()
        every { roomRepository.findById(id) }.returns(Optional.empty())
        assertThrows<NoSuchElementException> { sut.findOrdersById(id) }
        verify { roomRepository.findById(id) }
    }

    @Test
    fun findAllByDepartmentId_shouldInvokeService() {
        val id = UUID.randomUUID()
        val roomNorm = RoomNorm(
            id = UUID.randomUUID(),
            size = 6L,
            avgPersonNorm = 53.1F,
            createdAt = OffsetDateTime.now()
        )
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val room = Room(
            id = id,
            number = 1L,
            capacity = 3L,
            orders = emptyList(),
            roomNorm = roomNorm,
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
            number = room.number,
            size = roomNorm.size,
            capacity = room.capacity,
            avgPersonNorm = roomNorm.avgPersonNorm,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt
        )
        val result = sut.findAllByDepartmentId(id, pageable)
        assertThat(expected)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }
}