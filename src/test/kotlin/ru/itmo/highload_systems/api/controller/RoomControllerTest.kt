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
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.itmo.highload_systems.api.dto.OrderResponse
import ru.itmo.highload_systems.api.dto.OrderStatusRequestResponse
import ru.itmo.highload_systems.api.dto.RoomResponse
import ru.itmo.highload_systems.common.AbstractMvcTest
import ru.itmo.highload_systems.domain.service.RoomService
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [RoomController::class])
@AutoConfigureMockMvc(addFilters = false)
@Import(RoomControllerTest.RoomControllerTestConfig::class)
class RoomControllerTest : AbstractMvcTest() {

    @TestConfiguration
    internal class RoomControllerTestConfig {
        @Bean
        fun service() = mockk<RoomService>()
    }

    @Autowired
    private lateinit var roomService: RoomService

    @Test
    fun getRoomById_shouldInvokeServiceAndReturnResponse() {
        // given
        val id = "b797c253-d4ee-4da8-83bd-5b704b87bcb3"
        val expected = RoomResponse(
            id = UUID.fromString(id),
            number = 1L,
            size = 2L,
            capacity = 3L,
            avgPersonNorm = 4.1,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every {
            roomService.findById(UUID.fromString(id))
        }.returns(expected)

        // when
        val content = mockMvc.perform(
            get("/rooms/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)

        val result = objectMapper.readValue(content, RoomResponse::class.java)

        //then
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { roomService.findById(UUID.fromString(id)) }
    }

    @Test
    fun getOrdersByRoomId_shouldInvokeServiceAndReturnResponse() {
        // given
        val id = "b797c253-d4ee-4da8-83bd-5b704b87bcb3"
        val expected = OrderResponse(
            id = UUID.fromString(id),
            status = OrderStatusRequestResponse.NEW,
            size = 120L,
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomService.findOrdersById(UUID.fromString(id)) }
            .returns(listOf(expected))

        // when
        mockMvc.perform(
            get("/rooms/{id}/orders", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
        verify(exactly = 1) { roomService.findOrdersById(UUID.fromString(id)) }
    }
}