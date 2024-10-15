package ru.itmo.highload_systems.api.controller

import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.itmo.highload_systems.api.dto.OxygenSupplyResponse
import ru.itmo.highload_systems.common.AbstractMvcTest
import ru.itmo.highload_systems.domain.service.OxygenSupplyService
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [OxygenSupplyController::class])
@Import(OxygenSupplyControllerTest.OxygenSupplyControllerTestConfig::class)
class OxygenSupplyControllerTest : AbstractMvcTest() {
    @Autowired
    private lateinit var oxygenSupplyService: OxygenSupplyService

    @TestConfiguration
    internal class OxygenSupplyControllerTestConfig {
        @Bean
        fun service() = mockk<OxygenSupplyService>()
    }

    @Test
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

        // when
        val content = mockMvc.perform(
            get("/oxygen-supply/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.getContentAsString(StandardCharsets.UTF_8)
        val result = objectMapper.readValue(content, OxygenSupplyResponse::class.java)

        //then
        assertThat(result).isEqualTo(expected)
        verify(exactly = 1) { oxygenSupplyService.findById(UUID.fromString(id)) }
    }

    @Test
    fun getOxygenSupplies_shouldInvokeServiceAndResultResponse() {
        val expected = OxygenSupplyResponse(
            id = UUID.randomUUID(),
            size = 1L,
            oxygenStorageId = UUID.randomUUID(),
            departmentId = UUID.randomUUID(),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )

        // when
        val result = mockMvc.perform(
            get("/oxygen-supply")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        result.andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$[0].id").value(expected.id),
            jsonPath("$[0].size").value(expected.size),
            jsonPath("$[0].oxygenStorageId").value(expected.oxygenStorageId),
            jsonPath("$[0].departmentId").value(expected.departmentId),
            jsonPath("$[0].createdAt").value(expected.createdAt),
            jsonPath("$[0].updatedAt").value(expected.updatedAt)
        )
        verify(exactly = 1) { oxygenSupplyService.findAll(any()) }

    }
}