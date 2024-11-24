package ru.itmo.oxygen.api.controller

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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.itmo.oxygen.api.dto.OxygenStorageResponse
import ru.itmo.oxygen.common.AbstractMvcTest
import ru.itmo.oxygen.domain.service.OxygenStorageService
import ru.itmo.oxygen.infra.model.enums.Role
import ru.itmo.oxygen.security.WithMockUser
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [OxygenStorageController::class])
@AutoConfigureMockMvc
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
    @WithMockUser(role = Role.SUPPLIER)
    fun getStorages_shouldInvokeService() {
        val expected = OxygenStorageResponse(
            id = UUID.randomUUID(),
            size = 1L,
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
                .with(csrf())
        )
        verify(exactly = 1) { oxygenStorageService.findAll(any()) }
    }

    @Test
    @WithMockUser(role = Role.ADMIN)
    fun getStorageById_shouldInvokeService() {
        val id = "b797c253-d4ee-4da8-83bd-5b704b87bcb3"
        val expected = OxygenStorageResponse(
            id = UUID.fromString(id),
            size = 1L,
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
                .with(csrf())
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

    @Test
    @WithMockUser(role = Role.SUPPLIER)
    fun createStorage_shouldInvokeService() {
        val id = "b797c253-d4ee-4da8-83bd-5b704b87bcb3"
        val expected = OxygenStorageResponse(
            id = UUID.fromString(id),
            size = 1L,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { oxygenStorageService.create(1L) }.returns(expected)

        // when
        val content = mockMvc.perform(
            post("/storages")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OxygenStorageResponse::class.java)
        assertThat(result).usingComparatorForType(
            OffsetDateTimeByInstantComparator.getInstance(),
            OffsetDateTime::class.java
        )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { oxygenStorageService.create(1L) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
    fun createStorage_shouldThrowException() {
        // when
        mockMvc.perform(
            post("/storages")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden)
    }
}