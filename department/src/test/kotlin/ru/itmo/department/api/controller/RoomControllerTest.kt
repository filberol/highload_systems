package ru.itmo.department.api.controller

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
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.itmo.department.api.dto.RoomNormResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.common.AbstractMvcTest
import ru.itmo.department.domain.service.RoomService
import ru.itmo.department.infra.model.enums.Role
import ru.itmo.department.security.WithMockUser
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
    @WithMockUser(role = Role.MANAGER)
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
        every { roomService.findById(roomId) }.returns(Mono.just(expected))

        // when
        mockMvc.perform(
            get("/rooms/{id}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
        verify(exactly = 1) { roomService.findById(roomId) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
    fun getNormByRoomId_shouldInvokeServiceAndReturnResponse() {
        // given
        val roomId = UUID.randomUUID()
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = 5L,
            balanceOxygen = 10L,
            avgPersonNorm = 55,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomService.findWithNormById(roomId) }
            .returns(Mono.just(expected))

        // when
        mockMvc.perform(
            get("/rooms/{id}/norm", roomId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
        verify(exactly = 1) { roomService.findWithNormById(roomId) }
    }

    @Test
    @WithMockUser(role = Role.ADMIN)
    fun supplyOxygen_shouldInvokeServiceAndReturnResponse() {
        // given
        val roomId = UUID.randomUUID()
        val expected = RoomNormResponse(
            id = roomId,
            peopleCount = 5L,
            balanceOxygen = 10L,
            avgPersonNorm = 55,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { roomService.supplyOxygen(roomId, eq(5L)) }
            .returns(Mono.just(expected))
        every { roomService.findAllByDepartmentId(any(), any()) }
            .returns(Flux.empty())

        // when
        mockMvc.perform(
            post("/rooms/{id}/supply-oxygen", roomId.toString())
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)

        verify(exactly = 1) { roomService.supplyOxygen(roomId, 5L) }
    }
}