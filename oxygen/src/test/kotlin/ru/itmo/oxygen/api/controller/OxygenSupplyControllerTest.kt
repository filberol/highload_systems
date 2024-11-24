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
import ru.itmo.oxygen.api.dto.OxygenSupplyResponse
import ru.itmo.oxygen.common.AbstractMvcTest
import ru.itmo.oxygen.domain.service.OxygenSupplyService
import ru.itmo.oxygen.infra.model.enums.Role
import ru.itmo.oxygen.security.WithMockUser
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [OxygenSupplyController::class])
@AutoConfigureMockMvc
@Import(OxygenSupplyControllerTest.OxygenSupplyControllerTestConfig::class)
class OxygenSupplyControllerTest : AbstractMvcTest() {

    @TestConfiguration
    internal class OxygenSupplyControllerTestConfig {
        @Bean
        fun service() = mockk<OxygenSupplyService>()
    }

    @Autowired
    private lateinit var oxygenSupplyService: OxygenSupplyService

    @Test
    @WithMockUser(role = Role.SUPPLIER)
    fun getOxygenSupplyById_shouldInvokeServiceAndResultResponse() {
        val id = "b797c253-d4ee-4da8-83bd-5b704b87bcb3"
        val expected = OxygenSupplyResponse(
            id = UUID.fromString(id),
            size = 1L,
            oxygenStorageId = UUID.randomUUID(),
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { oxygenSupplyService.findById(UUID.fromString(id)) }
            .returns(expected)

        // when
        val content = mockMvc.perform(
            get("/oxygen-supply/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OxygenSupplyResponse::class.java)

        //then
        assertThat(result).usingComparatorForType(
            OffsetDateTimeByInstantComparator.getInstance(),
            OffsetDateTime::class.java
        )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { oxygenSupplyService.findById(UUID.fromString(id)) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
    fun getOxygenSupplies_shouldInvokeServiceAndResultResponse() {
        val expected = OxygenSupplyResponse(
            id = UUID.randomUUID(),
            size = 1L,
            oxygenStorageId = UUID.randomUUID(),
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )
        val page: Page<OxygenSupplyResponse> = PageImpl(listOf(expected), pageable, 1)
        every {
            oxygenSupplyService.findAll(
                pageable
            )
        }
            .returns(page)

        // when
        mockMvc.perform(
            get("/oxygen-supply")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        )
        verify(exactly = 1) { oxygenSupplyService.findAll(any()) }
    }

    @Test
    @WithMockUser(role = Role.MANAGER)
    fun create_shouldInvokeService() {
        val size = 50L
        val departmentId = "b797c253-d4ee-4da8-83bd-5b704b87bcb3"
        val expected = OxygenSupplyResponse(
            id = UUID.randomUUID(),
            size = 1L,
            oxygenStorageId = UUID.randomUUID(),
            departmentId = UUID.fromString(departmentId),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { oxygenSupplyService.create(size, UUID.fromString(departmentId)) }
            .returns(expected)

        // when
        val content = mockMvc.perform(
            post("/oxygen-supply")
                .param("size", "50")
                .param("departmentId", departmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OxygenSupplyResponse::class.java)
        assertThat(result).usingComparatorForType(
            OffsetDateTimeByInstantComparator.getInstance(),
            OffsetDateTime::class.java
        )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { oxygenSupplyService.create(size, UUID.fromString(departmentId)) }
    }

    @Test
    @WithMockUser(role = Role.SUPPLIER)
    fun process_shouldInvokeService() {
        val id = "b797c253-d4ee-4da8-83bd-5b704b87bcb3"
        val expected = OxygenSupplyResponse(
            id = UUID.fromString(id),
            size = 1L,
            oxygenStorageId = UUID.randomUUID(),
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { oxygenSupplyService.processById(UUID.fromString(id)) }
            .returns(expected)

        // when
        val content = mockMvc.perform(
            post("/oxygen-supply/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OxygenSupplyResponse::class.java)
        assertThat(result).usingComparatorForType(
            OffsetDateTimeByInstantComparator.getInstance(),
            OffsetDateTime::class.java
        )
            .usingRecursiveComparison()
            .isEqualTo(expected)
        verify(exactly = 1) { oxygenSupplyService.processById(UUID.fromString(id)) }
    }
}