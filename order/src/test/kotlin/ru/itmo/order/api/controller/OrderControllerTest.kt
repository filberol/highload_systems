package ru.itmo.order.api.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.order.api.dto.OrderResponse
import ru.itmo.order.api.dto.OrderStatusRequestResponse
import ru.itmo.order.clients.dto.CheckInResponse
import ru.itmo.order.common.AbstractMvcTest
import ru.itmo.order.domain.service.OrderService
import ru.itmo.order.infra.model.enums.Role
import ru.itmo.order.security.WithMockUser
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [OrderController::class])
@AutoConfigureMockMvc
@Import(OrderControllerTest.OrderControllerTestConfig::class)
class OrderControllerTest : AbstractMvcTest() {

    @TestConfiguration
    internal class OrderControllerTestConfig {
        @Bean
        fun service() = mockk<OrderService>()
    }

    @Autowired
    private lateinit var orderService: OrderService

    @Test
    @WithMockUser(role = Role.USER)
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
        mockMvc.perform(
            post("/orders")
                .param("departmentId", departmentId.toString())
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
        verify(exactly = 1) { orderService.create(departmentId, userId) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
    fun process_shouldInvokeService() {
        val id = UUID.randomUUID()
        val expected = CheckInResponse(
            departmentId = UUID.randomUUID(),
            personCount = 1L,
            roomId = UUID.randomUUID()
        )

        every { orderService.process(id, any()) }.returns(Mono.just(expected))

        // when
        mockMvc.perform(
            post("/orders/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Authorization")
                .with(csrf())
        ).andExpect(status().isOk)

        verify(exactly = 1) { orderService.process(id, any()) }
    }

    @Test
    @WithMockUser(role = Role.ADMIN)
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
        mockMvc.perform(
            post("/orders/cancel")
                .param("expiredAt", expiredAt.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
        verify(exactly = 1) { orderService.cancelExpiredOrders(expiredAt) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
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
        mockMvc.perform(
            post("/orders/{id}/cancel", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
        verify(exactly = 1) { orderService.cancelById(id) }
    }

    @Test
    @WithMockUser(role = Role.USER)
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
        mockMvc.perform(
            get("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
        verify(exactly = 1) { orderService.findAll(any()) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
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
        mockMvc.perform(
            get("/orders/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
        verify(exactly = 1) { orderService.findById(id) }
    }
}