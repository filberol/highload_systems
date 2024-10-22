package ru.itmo.highload_systems.domain.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import ru.itmo.highload_systems.api.dto.CreateOrderRequest
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.api.dto.OrderStatusRequestResponse
import ru.itmo.highload_systems.common.config.DomainMapperTestConfiguration
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.model.Order
import ru.itmo.highload_systems.infra.model.Room
import ru.itmo.highload_systems.infra.model.RoomNorm
import ru.itmo.highload_systems.infra.model.enums.OrderStatus
import ru.itmo.highload_systems.infra.repository.OrderRepository
import java.time.OffsetDateTime
import java.util.*


@WebMvcTest(controllers = [OrderService::class])
@Import(value = [DomainMapperTestConfiguration::class, OrderServiceTest.OrderServiceTestConfig::class])
class OrderServiceTest {

    @TestConfiguration
    internal class OrderServiceTestConfig {
        @Bean
        fun orderRepository() = mockk<OrderRepository>()

        @Bean
        fun departmentService() = mockk<DepartmentService>()

        @Bean
        fun roomNormService() = mockk<RoomNormService>()
    }

    @Autowired
    private lateinit var sut: OrderService

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var departmentService: DepartmentService

    @Autowired
    private lateinit var roomNormService: RoomNormService

    @Test
    fun create_shouldInvokeService() {
        val departmentId = UUID.randomUUID()
        val request = CreateOrderRequest(
            size = 5L,
            departmentId = departmentId
        )
        val orderId = UUID.randomUUID()
        val department = Department(
            id = departmentId,
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        every { departmentService.findById(departmentId) }
            .returns(department)
        val order = Order(
            size = request.size,
            status = OrderStatus.NEW,
            department = department
        )
        val createdAt = OffsetDateTime.now()
        val savedOrder = Order(
            id = orderId,
            size = request.size,
            status = OrderStatus.NEW,
            department = department,
            updatedAt = createdAt,
            createdAt = createdAt
        )
        every { orderRepository.save(any()) }.returns(savedOrder)
        val expected = with(savedOrder) {
            OrderResponse(
                id = orderId,
                status = OrderStatusRequestResponse.NEW,
                size = size,
                departmentId = departmentId,
                createdAt = createdAt,
                updatedAt = createdAt
            )
        }
        val result = sut.create(request)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }

    @Test
    fun process_shouldInvokeService() {
        val id = UUID.randomUUID()
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val order = Order(
            id = id,
            size = 5L,
            status = OrderStatus.NEW,
            department = department,
            updatedAt = OffsetDateTime.now(),
            createdAt = OffsetDateTime.now()
        )
        val roomNorm = RoomNorm(
            id = UUID.randomUUID(),
            size = 10L,
            avgPersonNorm = 53.1F,
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
        every { orderRepository.findById(id) }.returns(Optional.of(order))
        every { roomNormService.fillIfExistAndReturnRoom(any(), eq(5L)) }
            .returns(Optional.of(room))
        val saveOrder = Order(
            id = id,
            size = 5L,
            status = OrderStatus.DONE,
            department = department,
            updatedAt = order.updatedAt,
            createdAt = order.createdAt
        )
        every { orderRepository.save(any()) }.returns(saveOrder)
        val expected = OrderResponse(
            id = id,
            status = OrderStatusRequestResponse.DONE,
            size = 5L,
            departmentId = department.id!!,
            createdAt = order.createdAt!!,
            updatedAt = order.updatedAt!!
        )
        val result = sut.process(id)
        verify { orderRepository.findById(id) }
        verify { roomNormService.fillIfExistAndReturnRoom(department.id!!, 5L) }
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }

    @Test
    fun process_shouldInvokeServiceAndWaitingStatus_whenRoomNotExist() {
        val id = UUID.randomUUID()
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val order = Order(
            id = id,
            size = 5L,
            status = OrderStatus.NEW,
            department = department,
            updatedAt = OffsetDateTime.now(),
            createdAt = OffsetDateTime.now()
        )
        val roomNorm = RoomNorm(
            id = UUID.randomUUID(),
            size = 10L,
            avgPersonNorm = 53.1F,
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
        every { orderRepository.findById(id) }.returns(Optional.of(order))
        every { roomNormService.fillIfExistAndReturnRoom(department.id!!, 5L) }
            .returns(
                Optional.empty()
            )
        every { orderRepository.save(any()) }.returns(order)
        val expected = OrderResponse(
            id = id,
            status = OrderStatusRequestResponse.OXYGEN_WAITING,
            size = 5L,
            departmentId = department.id!!,
            createdAt = order.createdAt!!,
            updatedAt = order.updatedAt!!
        )
        val result = sut.process(id)
        verify { orderRepository.findById(id) }
        verify { roomNormService.fillIfExistAndReturnRoom(department.id!!, 5L) }
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }

    @Test
    fun cancelExpiredOrders_shouldInvokeRepository() {
        val expiredAt = OffsetDateTime.now()
        val status = OrderStatusRequestResponse.OXYGEN_WAITING
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val order = Order(
            id = UUID.randomUUID(),
            size = 5L,
            status = OrderStatus.OXYGEN_WAITING,
            department = department,
            updatedAt = OffsetDateTime.now(),
            createdAt = OffsetDateTime.now()
        )

        every {
            orderRepository.findAllByUpdatedAtLessThanAndStatusEquals(
                expiredAt,
                OrderStatus.OXYGEN_WAITING
            )
        }
            .returns(listOf(order))
        val savedOrder = with(order) {
            Order(
                id = id,
                size = size,
                status = OrderStatus.CANCEL,
                department = department,
                updatedAt = updatedAt,
                createdAt = createdAt
            )
        }
        every { orderRepository.saveAll(any<List<Order>>()) }.returns(listOf(savedOrder))
        val expected = with(savedOrder) {
            OrderResponse(
                id = id!!,
                size = size,
                status = OrderStatusRequestResponse.CANCEL,
                departmentId = department.id!!,
                updatedAt = updatedAt!!,
                createdAt = createdAt!!
            )
        }
        val result = sut.cancelExpiredOrders(expiredAt, status)

        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(listOf(expected))
        verify {
            orderRepository.findAllByUpdatedAtLessThanAndStatusEquals(
                expiredAt,
                OrderStatus.OXYGEN_WAITING
            )
        }
    }

    @Test
    fun cancelById_shouldInvokeService() {
        val id = UUID.randomUUID()
        val department = Department(
            id = id,
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val order = Order(
            id = UUID.randomUUID(),
            size = 5L,
            status = OrderStatus.OXYGEN_WAITING,
            department = department,
            updatedAt = OffsetDateTime.now(),
            createdAt = OffsetDateTime.now()
        )
        val savedOrder = with(order) {
            Order(
                id = id,
                size = size,
                status = OrderStatus.DONE,
                department = department,
                updatedAt = updatedAt,
                createdAt = createdAt
            )
        }
        val expected = with(savedOrder) {
            OrderResponse(
                id = id!!,
                size = size,
                status = OrderStatusRequestResponse.DONE,
                departmentId = department.id!!,
                updatedAt = updatedAt!!,
                createdAt = createdAt!!
            )
        }

        every { orderRepository.findById(id) }.returns(Optional.of(order))
        every { orderRepository.save(any()) }.returns(savedOrder)
        val result = sut.cancelById(id)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify { orderRepository.findById(id) }
        verify { orderRepository.save(any()) }
    }


    @Test
    fun findById_shouldInvokeService() {
        val id = UUID.randomUUID()
        val department = Department(
            id = id,
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val order = Order(
            id = UUID.randomUUID(),
            size = 5L,
            status = OrderStatus.OXYGEN_WAITING,
            department = department,
            updatedAt = OffsetDateTime.now(),
            createdAt = OffsetDateTime.now()
        )
        every { orderRepository.findById(id) }
            .returns(Optional.of(order))
        val result = sut.findById(id)
        val expected = with(order) {
            OrderResponse(
                id = order.id!!,
                size = size,
                status = OrderStatusRequestResponse.OXYGEN_WAITING,
                departmentId = department.id!!,
                updatedAt = updatedAt!!,
                createdAt = createdAt!!
            )
        }
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify { orderRepository.findById(id) }
    }
}