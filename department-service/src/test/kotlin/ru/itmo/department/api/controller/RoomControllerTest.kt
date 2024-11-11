package ru.itmo.department.api.controller

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
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.common.AbstractMvcTest
import ru.itmo.department.domain.service.RoomService
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
        fun roomService() = mockk<RoomService>()
    }

    @Autowired
    private lateinit var roomService: RoomService

    @Test
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
        every { roomService.findById(roomId) }.returns(expected)

        // when
        val content = mockMvc.perform(
            get("/rooms/{id}", roomId)
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
        verify(exactly = 1) { roomService.findById(roomId) }
    }

    @Test
    fun getNormByRoomId_shouldInvokeServiceAndReturnResponse() {
        // given
        val roomId = UUID.randomUUID()
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = 5L,
            balanceOxygen = 10L,
            avgPersonNorm = 55.55,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomService.findWithNormById(roomId) }
            .returns(expected)

        // when
        val content = mockMvc.perform(
            get("/rooms/{id}/norm", roomId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)

        // then
        val result = objectMapper.readValue(content, RoomNormResponse::class.java)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { roomService.findWithNormById(roomId) }
    }

    @Test
    fun supplyOxygen_shouldInvokeServiceAndReturnResponse() {
        // given
        val roomId = UUID.randomUUID()
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = 5L,
            balanceOxygen = 10L,
            avgPersonNorm = 55.55,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomService.supplyOxygen(roomId, eq(5L)) }
            .returns(expected)
        every { roomService.findAllByDepartmentId(any(), any()) }
            .returns(Page.empty())

        // when
        val content = mockMvc.perform(
            post("/rooms/{id}/supply-oxygen", roomId.toString())
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)

        // then
        val result = objectMapper.readValue(content, RoomNormResponse::class.java)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { roomService.supplyOxygen(roomId, 5L) }
    }
}