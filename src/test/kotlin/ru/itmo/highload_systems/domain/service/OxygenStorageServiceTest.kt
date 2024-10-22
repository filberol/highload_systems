package ru.itmo.highload_systems.domain.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import ru.itmo.highload_systems.api.dto.OxygenStorageResponse
import ru.itmo.highload_systems.common.config.DomainMapperTestConfiguration
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.model.OxygenStorage
import ru.itmo.highload_systems.infra.repository.OxygenStorageRepository
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [OxygenStorageService::class])
@Import(value = [DomainMapperTestConfiguration::class, OxygenStorageServiceTest.OxygenStorageServiceTestConfig::class])
class OxygenStorageServiceTest {

    @TestConfiguration
    internal class OxygenStorageServiceTestConfig {
        @Bean
        fun oxygenStorageRepository() = mockk<OxygenStorageRepository>()
    }

    @Autowired
    private lateinit var sut: OxygenStorageService

    @Autowired
    private lateinit var oxygenStorageRepository: OxygenStorageRepository

    @Test
    fun findById_shouldInvokeRepository() {
        val id = UUID.randomUUID()
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val oxygenStorage = OxygenStorage(
            id = id,
            size = 5L,
            capacity = 10L,
            department = department,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val expected = OxygenStorageResponse(
            id = id,
            size = 5L,
            capacity = 10L,
            departmentId = department.id!!,
            createdAt = oxygenStorage.createdAt!!,
            updatedAt = oxygenStorage.updatedAt!!
        )
        every { oxygenStorageRepository.findById(id) }.returns(Optional.of(oxygenStorage))
        val result = sut.findById(id)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }
}