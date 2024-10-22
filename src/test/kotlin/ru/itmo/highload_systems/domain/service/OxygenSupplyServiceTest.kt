package ru.itmo.highload_systems.domain.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import ru.itmo.highload_systems.api.dto.OxygenSupplyResponse
import ru.itmo.highload_systems.common.config.DomainMapperTestConfiguration
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.model.OxygenStorage
import ru.itmo.highload_systems.infra.model.OxygenSupply
import ru.itmo.highload_systems.infra.repository.OxygenStorageRepository
import ru.itmo.highload_systems.infra.repository.OxygenSupplyRepository
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [OxygenSupplyService::class])
@Import(value = [DomainMapperTestConfiguration::class, OxygenSupplyServiceTest.OxygenSupplyServiceTestConfig::class])
class OxygenSupplyServiceTest {

    @TestConfiguration
    internal class OxygenSupplyServiceTestConfig {
        @Bean
        fun oxygenSupplyRepository() = mockk<OxygenSupplyRepository>()

        @Bean
        fun departmentService() = mockk<DepartmentService>()

        @Bean
        fun oxygenStorageRepository() = mockk<OxygenStorageRepository>()
    }

    @Autowired
    private lateinit var sut: OxygenSupplyService

    @Autowired
    private lateinit var oxygenSupplyRepository: OxygenSupplyRepository

    @Autowired
    private lateinit var departmentService: DepartmentService

    @Autowired
    private lateinit var oxygenStorageRepository: OxygenStorageRepository

    @Test
    fun create_shouldInvokeService() {
        val size = 5L
        val departmentId = UUID.randomUUID()
        val id = UUID.randomUUID()
        val createdAt = OffsetDateTime.now()
        val department = Department(
            id = departmentId,
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        every { departmentService.findById(departmentId) }.returns(department)
        val saveSupply = OxygenSupply(
            size = 5L,
            department = department,
            id = id,
            createdAt = createdAt,
            updatedAt = createdAt
        )
        every { oxygenSupplyRepository.save(any()) }.returns(saveSupply)
        val expected = OxygenSupplyResponse(
            id = id,
            size = size,
            departmentId = departmentId,
            createdAt = createdAt,
            updatedAt = createdAt
        )
        val result = sut.create(size, departmentId)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::
                class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }

    @Test
    fun create_shouldThrowException_whenDepartmentNotExist() {
        val size = 5L
        val departmentId = UUID.randomUUID()
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        every { departmentService.findById(departmentId) }.throws(NoSuchElementException())

        assertThrows<NoSuchElementException> { sut.create(size, departmentId) }
        verify { departmentService.findById(departmentId) }
    }

    @Test
    fun processById_shouldInvokeService() {
        val size = 5L
        val departmentId = UUID.randomUUID()
        val department = Department(
            id = departmentId,
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val id = UUID.randomUUID()
        val supply = OxygenSupply(
            id = id,
            size = 3L,
            department = department,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        val oxygenStorage = OxygenStorage(
            id = UUID.randomUUID(),
            size = 5L,
            capacity = 10L,
            department = department,
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        every { oxygenSupplyRepository.findById(id) }
            .returns(Optional.of(supply))
        every { oxygenStorageRepository.findByDepartmentIdAndCapacityGreaterThan(departmentId, 3L) }
            .returns(Optional.of(oxygenStorage))
        val saveOxygenStorage = with(oxygenStorage) {
            OxygenStorage(
                id = id,
                size = size + 3L,
                capacity = capacity,
                department = department,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
        every { oxygenStorageRepository.save(any()) }.returns(saveOxygenStorage)
        every { oxygenSupplyRepository.save(any()) }.returns(supply)
        val expected = with(supply) {
            OxygenSupplyResponse(
                id = id,
                size = 3L,
                oxygenStorageId = oxygenStorage.id,
                departmentId = departmentId,
                createdAt = createdAt!!,
                updatedAt = updatedAt!!
            )
        }
        val result = sut.processById(id)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::
                class.java
            )
            .usingRecursiveComparison()
            .ignoringFields("updatedAt")
            .isEqualTo(expected)
    }
}