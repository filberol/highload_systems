package ru.itmo.highload_systems.api.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.itmo.highload_systems.api.dto.CreateOrderRequest
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.api.dto.OrderStatusRequestResponse
import ru.itmo.highload_systems.common.AbstractMvcTest
import ru.itmo.highload_systems.domain.service.OrderService
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [OrderController::class])
@AutoConfigureMockMvc(addFilters = false)
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
    fun create_shouldInvokeService() {
        val departmentId = UUID.randomUUID()
        val request = CreateOrderRequest(5L, departmentId)
        val expected = OrderResponse(
            id = UUID.randomUUID(),
            status = OrderStatusRequestResponse.NEW,
            size = 5L,
            departmentId = departmentId,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { orderService.create(request) }.returns(expected)

        // when
        val content = mockMvc.perform(
            post("/orders")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OrderResponse::class.java)
        assertThat(result).usingComparatorForType(
            OffsetDateTimeByInstantComparator.getInstance(),
            OffsetDateTime::class.java
        )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { orderService.create(request) }
    }

    @Test
    fun process_shouldInvokeService() {
        val id = UUID.randomUUID()
        val expected = OrderResponse(
            id = id,
            status = OrderStatusRequestResponse.NEW,
            size = 5L,
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )

        every { orderService.process(id) }.returns(expected)

        // when
        val content = mockMvc.perform(
            post("/orders/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OrderResponse::class.java)
        assertThat(result).usingComparatorForType(
            OffsetDateTimeByInstantComparator.getInstance(),
            OffsetDateTime::class.java
        )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { orderService.process(id) }
    }

    @Test
    fun cancel_shouldInvokeService() {
        val expiredAt = OffsetDateTime.now()
        val status = OrderStatusRequestResponse.CANCEL
        val expected = OrderResponse(
            id = UUID.randomUUID(),
            status = OrderStatusRequestResponse.NEW,
            size = 5L,
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { orderService.cancelExpiredOrders(expiredAt, status) }
            .returns(listOf(expected))

        // when
        mockMvc.perform(
            post("/orders/cancel")
                .param("expiredAt", expiredAt.toString())
                .param("status", "CANCEL")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        verify(exactly = 1) { orderService.cancelExpiredOrders(expiredAt, status) }
    }

    @Test
    fun cancelById_shouldInvokeService() {
        val id = UUID.randomUUID()
        val expected = OrderResponse(
            id = id,
            status = OrderStatusRequestResponse.CANCEL,
            size = 5L,
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { orderService.cancelById(id) }.returns(expected)
        // when
        val content = mockMvc.perform(
            post("/orders/{id}/cancel", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OrderResponse::class.java)
        assertThat(result).usingComparatorForType(
            OffsetDateTimeByInstantComparator.getInstance(),
            OffsetDateTime::class.java
        )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { orderService.cancelById(id) }
    }

    @Test
    fun getOrders_shouldInvokeService() {
        val expected = OrderResponse(
            id = UUID.randomUUID(),
            status = OrderStatusRequestResponse.CANCEL,
            size = 5L,
            departmentId = UUID.randomUUID(),
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
            .returns(page)

        // when
        mockMvc.perform(
            get("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        verify(exactly = 1) { orderService.findAll(any()) }
    }

    @Test
    fun getOrderById_shouldInvokeService() {
        val id = UUID.randomUUID()
        val expected = OrderResponse(
            id = id,
            status = OrderStatusRequestResponse.CANCEL,
            size = 5L,
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { orderService.findById(id) }.returns(expected)
        // when
        val content = mockMvc.perform(
            get("/orders/{id}", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OrderResponse::class.java)
        assertThat(result).usingComparatorForType(
            OffsetDateTimeByInstantComparator.getInstance(),
            OffsetDateTime::class.java
        )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { orderService.findById(id) }
    }
}