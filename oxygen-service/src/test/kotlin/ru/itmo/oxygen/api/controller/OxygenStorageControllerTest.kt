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
import ru.itmo.highload_systems.api.dto.OxygenStorageResponse
import ru.itmo.highload_systems.common.AbstractMvcTest
import ru.itmo.highload_systems.domain.service.OxygenStorageService
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [OxygenStorageController::class])
@AutoConfigureMockMvc(addFilters = false)
@Import(OxygenStorageControllerTest.OxygenStorageControllerTestConfig::class)
class OxygenStorageControllerTest : AbstractMvcTest() {


    @TestConfiguration
    internal class OxygenStorageControllerTestConfig {
        @Bean
        fun service() = mockk<OxygenStorageService>()
    }

    @Autowired
    private lateinit var oxygenStorageService: OxygenStorageService


    @Test
    fun getStorages_shouldInvokeService() {
        val expected = OxygenStorageResponse(
            id = UUID.randomUUID(),
            size = 1L,
            capacity = 50L,
            departmentId = UUID.randomUUID(),
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
            oxygenStorageService.findAll(
                pageable
            )
        }
            .returns(page)

        // when
        mockMvc.perform(
            get("/storages")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        verify(exactly = 1) { oxygenStorageService.findAll(any()) }
    }

    @Test
    fun getStorageById_shouldInvokeService() {
        val id = "b797c253-d4ee-4da8-83bd-5b704b87bcb3"
        val expected = OxygenStorageResponse(
            id = UUID.fromString(id),
            size = 1L,
            capacity = 50L,
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { oxygenStorageService.findById(UUID.fromString(id)) }
            .returns(expected)

        // when
        val content = mockMvc.perform(
            get("/storages/{id}", id)
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
        verify(exactly = 1) { oxygenStorageService.findById(UUID.fromString(id)) }
    }
}