package ru.itmo.highload_systems.domain.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.internal.OffsetDateTimeByInstantComparator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
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

    @Test
    fun findById_shouldThrowException() {
        val id = UUID.randomUUID()
        every { oxygenStorageRepository.findById(id) }.returns(Optional.empty())
        assertThrows<NoSuchElementException> { sut.findById(id) }
    }


    @Test
    fun findByDepartmentId_shouldInvokeRepository() {
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
        every { oxygenStorageRepository.findByDepartmentId(id) }.returns(Optional.of(oxygenStorage))
        val result = sut.findByDepartmentId(id)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }

    @Test
    fun findByDepartmentId_shouldThrowException() {
        val id = UUID.randomUUID()
        every { oxygenStorageRepository.findByDepartmentId(id) }.returns(Optional.empty())
        assertThrows<NoSuchElementException> { sut.findByDepartmentId(id) }
    }

    @Test
    fun findAll_shouldInvokeRepository() {
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
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )
        val page: Page<OxygenStorage> = PageImpl(listOf(oxygenStorage), pageable, 1)
        every { oxygenStorageRepository.findAll(pageable) }.returns(page)
        val result = sut.findAll(pageable)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(PageImpl(listOf(expected), pageable, 1))
    }
}