package ru.itmo.order.api.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import ru.itmo.order.api.dto.OrderResponse
import ru.itmo.order.api.dto.OrderStatusRequestResponse
import ru.itmo.order.clients.dto.CheckInResponse
import ru.itmo.order.domain.service.OrderService
import java.time.OffsetDateTime
import java.util.*

class OrderControllerTest {
    private val orderService = mockk<OrderService>()
    private val sut = OrderController(orderService)

    @Test
    fun create_shouldInvokeService() {
        val departmentId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val expected = OrderResponse(
            id = UUID.randomUUID(),
            status = OrderStatusRequestResponse.NEW,
            departmentId = departmentId,
            userId = userId,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { orderService.create(departmentId, userId) }.returns(Flux.just(expected))

        // when
        val result = sut.create(departmentId, userId)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
        verify(exactly = 1) { orderService.create(departmentId, userId) }
    }

    @Test
    fun process_shouldInvokeService() {
        val id = UUID.randomUUID()
        val expected = CheckInResponse(
            departmentId = UUID.randomUUID(),
            personCount = 1L,
            roomId = UUID.randomUUID()
        )

        every { orderService.process(id, any()) }.returns(Mono.just(expected))

        // when
        val result = sut.process("token", id)
        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()

        verify(exactly = 1) { orderService.process(id, any()) }
    }

    @Test
    fun cancel_shouldInvokeService() {
        val expiredAt = OffsetDateTime.now()
        val userId = UUID.randomUUID()

        val expected = OrderResponse(
            id = UUID.randomUUID(),
            status = OrderStatusRequestResponse.NEW,
            departmentId = UUID.randomUUID(),
            userId = userId,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { orderService.cancelExpiredOrders(expiredAt) }
            .returns(Flux.fromIterable((listOf(expected))))

        // when
        val result = sut.cancel(expiredAt)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
        verify(exactly = 1) { orderService.cancelExpiredOrders(expiredAt) }
    }

    @Test
    fun cancelById_shouldInvokeService() {
        val id = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val expected = OrderResponse(
            id = id,
            status = OrderStatusRequestResponse.CANCEL,
            departmentId = UUID.randomUUID(),
            userId = userId,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { orderService.cancelById(id) }.returns(Mono.just(expected))
        // when
        val result = sut.cancelById(id)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
        verify(exactly = 1) { orderService.cancelById(id) }
    }

    @Test
    fun getOrders_shouldInvokeService() {
        val userId = UUID.randomUUID()

        val expected = OrderResponse(
            id = UUID.randomUUID(),
            status = OrderStatusRequestResponse.CANCEL,
            departmentId = UUID.randomUUID(),
            userId = userId,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )
        val page: Page<OrderResponse> = PageImpl(listOf(expected), pageable, 1)
        every {
            orderService.findAll(
                pageable
            )
        }
            .returns(Flux.just(page))

        // when
        val result = sut.getOrders(pageable)

        // then
        StepVerifier.create(result)
            .expectNext(page)
            .verifyComplete()
        verify(exactly = 1) { orderService.findAll(any()) }
    }

    @Test
    fun getOrderById_shouldInvokeService() {
        val id = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val expected = OrderResponse(
            id = id,
            status = OrderStatusRequestResponse.CANCEL,
            departmentId = UUID.randomUUID(),
            userId = userId,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { orderService.findById(id) }.returns(Mono.just(expected))
        // when
        val result = sut.getOrderById(id)

        // then
        StepVerifier.create(result)
            .expectNext(expected)
            .verifyComplete()
        verify(exactly = 1) { orderService.findById(id) }
    }
}