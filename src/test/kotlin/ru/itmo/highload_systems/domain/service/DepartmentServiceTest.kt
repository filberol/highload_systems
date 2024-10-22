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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import ru.itmo.highload_systems.api.dto.DepartmentResponse
import ru.itmo.highload_systems.common.config.DomainMapperTestConfiguration
import ru.itmo.highload_systems.infra.model.Department
import ru.itmo.highload_systems.infra.repository.DepartmentRepository
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(controllers = [DepartmentService::class])
@Import(value = [DomainMapperTestConfiguration::class, DepartmentServiceTest.DepartmentServiceTestConfig::class])
class DepartmentServiceTest {

    @TestConfiguration
    internal class DepartmentServiceTestConfig {

        @Bean
        fun departmentRepository() = mockk<DepartmentRepository>()
    }

    @Autowired
    private lateinit var sut: DepartmentService

    @Autowired
    private lateinit var departmentRepository: DepartmentRepository

    @Test
    fun getDepartments_shouldInvokeRepository() {
        val department = Department(
            id = UUID.randomUUID(),
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        val pageable = PageRequest.of(
            0,
            50,
            Sort.by(Direction.ASC, "id")
        )
        val page: Page<Department> = PageImpl(listOf(department), pageable, 1)
        every { departmentRepository.findAll(pageable) }
            .returns(page)
        val expected = DepartmentResponse(
            id = department.id!!,
            name = department.name,
            createdAt = department.createdAt
        )
        val result = sut.getDepartments(pageable)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(PageImpl(listOf(department), pageable, 1))
    }

    @Test
    fun findById_shouldInvokeRepository() {
        val id = UUID.randomUUID()
        val department = Department(
            id = id,
            name = "department",
            oxygenStorages = emptyList(),
            rooms = emptyList(),
            createdAt = OffsetDateTime.now()
        )
        every { departmentRepository.findById(id) }
            .returns(Optional.of(department))
        val result = sut.findById(id)
        assertThat(result)
            .usingComparatorForType(
                OffsetDateTimeByInstantComparator.getInstance(),
                OffsetDateTime::class.java
            )
            .usingRecursiveComparison()
            .isEqualTo(department)
    }
}