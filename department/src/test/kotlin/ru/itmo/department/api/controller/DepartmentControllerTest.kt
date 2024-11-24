package ru.itmo.department.api.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
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
import ru.itmo.department.api.dto.CheckInResponse
import ru.itmo.department.api.dto.DepartmentResponse
import ru.itmo.department.api.dto.RoomResponse
import ru.itmo.department.common.AbstractMvcTest
import ru.itmo.department.domain.service.DepartmentService
import ru.itmo.department.domain.service.RoomService
import ru.itmo.department.infra.model.enums.Role
import ru.itmo.department.security.WithMockUser
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [DepartmentController::class])
@AutoConfigureMockMvc(addFilters = false)
@Import(DepartmentControllerTest.DepartmentControllerTestConfig::class)
class DepartmentControllerTest : AbstractMvcTest() {

    @TestConfiguration
    internal class DepartmentControllerTestConfig {
        @Bean
        fun departmentService() = mockk<DepartmentService>()

        @Bean
        fun roomService() = mockk<RoomService>()
    }

    @Autowired
    private lateinit var departmentService: DepartmentService

    @Autowired
    private lateinit var roomService: RoomService


    @Test
    @WithMockUser(role = Role.USER)
    fun getDepartments_shouldInvokeService() {
        val expected = DepartmentResponse(
            id = UUID.randomUUID(),
            name = "department",
            createdAt = OffsetDateTime.now()
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )
        val page: Page<DepartmentResponse> = PageImpl(listOf(expected), pageable, 1)
        every {
            departmentService.getDepartments(
                pageable
            )
        }
            .returns(page)
        mockMvc.perform(
            get("/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        )
        verify(exactly = 1) { departmentService.getDepartments(any()) }
    }

    @Test
    @WithMockUser(role = Role.USER)
    fun getRooms_shouldInvokeService() {
        val id = UUID.randomUUID()
        val expected = RoomResponse(
            id = UUID.randomUUID(),
            departmentId = id,
            capacity = 5L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )
        val page: Page<RoomResponse> = PageImpl(listOf(expected), pageable, 1)
        every {
            roomService.findAllByDepartmentId(
                id,
                pageable
            )
        }
            .returns(page)
        mockMvc.perform(
            get("/departments/{id}/rooms", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        )
        verify(exactly = 1) { roomService.findAllByDepartmentId(eq(id), any()) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
    fun checkIn_shouldInvokeService() {
        // given
        val departmentId = UUID.randomUUID()
        val roomId = UUID.randomUUID()
        val personCount = 5L
        val expected = CheckInResponse(
            departmentId = departmentId,
            roomId = roomId,
            personCount = personCount
        )
        every { departmentService.checkIn(departmentId) }
            .returns(expected)
        // when
        val content = mockMvc.perform(
            post("/departments/{id}/check-in", departmentId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, CheckInResponse::class.java)

        // then
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { departmentService.checkIn(departmentId) }
    }
}