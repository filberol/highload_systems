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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.itmo.highload_systems.api.dto.DepartmentResponse
import ru.itmo.highload_systems.api.dto.OxygenStorageResponse
import ru.itmo.highload_systems.api.dto.RoomResponse
import ru.itmo.highload_systems.common.AbstractMvcTest
import ru.itmo.highload_systems.domain.service.DepartmentService
import ru.itmo.highload_systems.domain.service.OxygenStorageService
import ru.itmo.highload_systems.domain.service.RoomService
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

        @Bean
        fun storageService() = mockk<OxygenStorageService>()
    }

    @Autowired
    private lateinit var departmentService: DepartmentService

    @Autowired
    private lateinit var roomService: RoomService

    @Autowired
    private lateinit var storageService: OxygenStorageService

    @Test
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
        )
        verify(exactly = 1) { departmentService.getDepartments(any()) }
    }

    @Test
    fun getRooms_shouldInvokeService() {
        val id = UUID.randomUUID()
        val expected = RoomResponse(
            id = UUID.randomUUID(),
            number = 1L,
            size = 2L,
            capacity = 3L,
            avgPersonNorm = 4.1,
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
        )
        verify(exactly = 1) { roomService.findAllByDepartmentId(eq(id), any()) }
    }

    @Test
    fun getStorage_shouldInvokeService() {
        val id = UUID.randomUUID()
        val expected = OxygenStorageResponse(
            id = UUID.randomUUID(),
            size = 1L,
            capacity = 50L,
            departmentId = id,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )
        val page: Page<OxygenStorageResponse> = PageImpl(listOf(expected), pageable, 1)
        every {
            storageService.findByDepartmentId(
                id
            )
        }
            .returns(expected)
        val content = mockMvc.perform(
            get("/departments/{id}/storage", id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OxygenStorageResponse::class.java)
        assertThat(result).usingComparatorForType(
            OffsetDateTimeByInstantComparator.getInstance(),
            OffsetDateTime::class.java
        )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { storageService.findByDepartmentId(id) }
    }
}